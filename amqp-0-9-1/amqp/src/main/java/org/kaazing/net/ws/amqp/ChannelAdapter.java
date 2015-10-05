/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.kaazing.net.ws.amqp;

/**
 * Convenience adapter for ChannelListener
 */
public class ChannelAdapter implements ChannelListener {

    @Override
    public void onBindQueue(ChannelEvent e) {
    }

    @Override
    public void onCancelBasic(ChannelEvent e) {
    }

    @Override
    public void onClose(ChannelEvent e) {
    }

    @Override
    public void onCommit(ChannelEvent e) {
    }

    @Override
    public void onConsumeBasic(ChannelEvent e) {
    }

    @Override
    public void onDeclareExchange(ChannelEvent e) {
    }
    
    @Override
    public void onDeclareQueue(ChannelEvent e) {
    }

    @Override
    public void onDeleteExchange(ChannelEvent e) {
    }
    
    @Override
    public void onDeleteQueue(ChannelEvent e) {
    }

    @Override
    public void onDeliver(ChannelEvent e) {
    }

    @Override
    public void onError(ChannelEvent e) {
    }

    @Override
    public void onFlow(ChannelEvent e) {
    }

    @Override
    public void onGetBasic(ChannelEvent e) {
    }

    @Override
    public void onGetEmpty(ChannelEvent e) {
    }

    @Override
    public void onOpen(ChannelEvent e) {
    }

    @Override
    public void onPurgeQueue(ChannelEvent e) {
    }

    @Override
    public void onQos(ChannelEvent e) {
    }

    @Override
    public void onRecoverBasic(ChannelEvent e) {
    }

    @Override
    public void onRollback(ChannelEvent e) {
    }

    @Override
    public void onSelect(ChannelEvent e) {
    }

    @Override
    public void onUnbind(ChannelEvent e) {
    }

    @Override
    public void onMessage(ChannelEvent e) {
    }

    @Override
    public void onRejectBasic(ChannelEvent e) {
    }

}
