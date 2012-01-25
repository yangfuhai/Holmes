/**
* Copyright (c) 2012 Cedric Cheneau
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package net.holmes.core.service;

import java.util.Set;

import net.holmes.core.common.INodeListener;
import net.holmes.core.model.AbstractNode;

/**
 * The Interface IMediaService.
 */
public interface IMediaService
{

    /**
     * Get a specific node. Return null is not found
     *
     * @param nodeId the node id
     * @return the node
     */
    public abstract AbstractNode getNode(String nodeId);

    /**
     * Gets the node ids.
     *
     * @return the node ids
     */
    public abstract Set<String> getNodeIds();

    /**
     * Scan all media.
     * 
     */
    public abstract void scanAll();

    /**
     * Scan videos.
     *
     */
    public abstract void scanVideos();

    /**
     * Scan audios.
     *
     */
    public abstract void scanAudios();

    /**
     * Scan pictures.
     *
     */
    public abstract void scanPictures();

    /**
     * Scan podcasts.
     *
     */
    public abstract void scanPodcasts();

    /**
     * Adds the add content node listener.
     *
     * @param listener the listener
     */
    public abstract void addAddContentNodeListener(INodeListener listener);
}