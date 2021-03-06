/**
* Copyright (C) 2012  Cedric Cheneau
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.holmes.core.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServerHandler redirect Http requests to proper handler
 */
public final class HttpRequestHandler extends SimpleChannelHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);

    private final IHttpRequestHandler contentRequestHandler;
    private final IHttpRequestHandler backendRequestHandler;
    private final IHttpRequestHandler siteRequestHandler;

    @Inject
    public HttpRequestHandler(@Named("content") IHttpRequestHandler contentRequestHandler, @Named("backend") IHttpRequestHandler backendRequestHandler,
            @Named("site") IHttpRequestHandler siteRequestHandler) {
        this.contentRequestHandler = contentRequestHandler;
        this.backendRequestHandler = backendRequestHandler;
        this.siteRequestHandler = siteRequestHandler;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws IOException {
        HttpRequest request = (HttpRequest) e.getMessage();

        if (logger.isDebugEnabled()) {
            logger.debug("[START] messageReceived event:" + e);
            logger.debug("Request uri: " + request.getUri());
            for (Entry<String, String> entry : request.getHeaders()) {
                logger.debug("Request header: " + entry.getKey() + " ==> " + entry.getValue());
            }

            if (request.getMethod().equals(HttpMethod.POST)) {
                ChannelBuffer content = request.getContent();
                if (content.readable()) {
                    QueryStringDecoder queryStringDecoder = new QueryStringDecoder("/?" + content.toString(Charset.forName("utf-8")));
                    Map<String, List<String>> params = queryStringDecoder.getParameters();
                    if (params != null) {
                        for (String paramKey : params.keySet()) {
                            logger.debug("Post parameter: " + paramKey + " => " + params.get(paramKey));
                        }
                    }
                }
            }
        }

        String requestPath = new QueryStringDecoder(request.getUri()).getPath();
        try {
            // Dispatch request to proper handler
            if (contentRequestHandler.canProcess(requestPath, request.getMethod())) contentRequestHandler.processRequest(request, e.getChannel());
            else if (backendRequestHandler.canProcess(requestPath, request.getMethod())) backendRequestHandler.processRequest(request, e.getChannel());
            else if (siteRequestHandler.canProcess(requestPath, request.getMethod())) siteRequestHandler.processRequest(request, e.getChannel());
            else sendError(ctx, HttpResponseStatus.BAD_REQUEST);

        } catch (HttpRequestException ex) {
            sendError(ctx, ex.getStatus());
        }

        if (logger.isDebugEnabled()) logger.debug("[END] messageReceived");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent event) throws Exception {
        Channel channel = event.getChannel();
        Throwable cause = event.getCause();
        if (cause instanceof TooLongFrameException) {
            sendError(context, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if (channel.isConnected() && !event.getFuture().isSuccess()) {
            sendError(context, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendError(ChannelHandlerContext context, HttpResponseStatus status) {
        // Build response
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
        response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ChannelBuffer buffer = ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8);
        response.setContent(buffer);

        if (logger.isDebugEnabled()) logger.debug("sendError: " + buffer);

        // Close the connection as soon as the error message is sent.
        context.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }
}