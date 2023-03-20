/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.ai.msg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * A {@code MessageDispatcher} is in charge of the creation, dispatch, and management of telegrams.
 *
 * @author davebaol
 */
public class MessageDispatcher implements Telegraph {

    private static final String LOG_TAG = MessageDispatcher.class.getSimpleName();

    private static final Pool<Telegram> POOL = new Pool<Telegram>(16) {
        @Override
        protected Telegram newObject() {
            return new Telegram();
        }
    };

    private PriorityQueue<Telegram> queue;

    private ObjectMap<String, Array<Telegraph>> msgListeners;

    private ObjectMap<String, Array<TelegramProvider>> msgProviders;

    private boolean debugEnabled;
    private int queueIndex = 0;

    /**
     * Creates a {@code MessageDispatcher}
     */
    public MessageDispatcher() {
        this.queue = new PriorityQueue<>();
        this.msgListeners = new ObjectMap<>();
        this.msgProviders = new ObjectMap<>();
    }

    /**
     * Returns true if debug mode is on; false otherwise.
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Sets debug mode on/off.
     */
    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    /**
     * Registers a listener for the specified message code. Messages without an explicit receiver are broadcasted to all its
     * registered listeners.
     *
     * @param listener the listener to add
     * @param msg      the message code
     */
    public void addListener(Telegraph listener, String msg) {
        Array<Telegraph> listeners = msgListeners.get(msg);
        if (listeners == null) {
            // Associate an empty unordered array with the message code
            listeners = new Array<Telegraph>(false, 16);
            msgListeners.put(msg, listeners);
        }
        listeners.add(listener);

        // Dispatch messages from registered providers
        Array<TelegramProvider> providers = msgProviders.get(msg);
        if (providers != null) {
            for (int i = 0, n = providers.size; i < n; i++) {
                TelegramProvider provider = providers.get(i);
                Object info = provider.provideMessageInfo(msg, listener);
                if (info != null) {
                    Telegraph sender = ClassReflection.isInstance(Telegraph.class, provider) ? (Telegraph) provider : null;
                    dispatchMessage(0, sender, listener, msg, info, false);
                }
            }
        }
    }

    /**
     * Registers a listener for a selection of message types. Messages without an explicit receiver are broadcasted to all its
     * registered listeners.
     *
     * @param listener the listener to add
     * @param msgs     the message codes
     */
    public void addListeners(Telegraph listener, String... msgs) {
        for (String msg : msgs)
            addListener(listener, msg);
    }

    /**
     * Registers a provider for the specified message code.
     *
     * @param msg      the message code
     * @param provider the provider to add
     */
    public void addProvider(TelegramProvider provider, String msg) {
        Array<TelegramProvider> providers = msgProviders.get(msg);
        if (providers == null) {
            // Associate an empty unordered array with the message code
            providers = new Array<TelegramProvider>(false, 16);
            msgProviders.put(msg, providers);
        }
        providers.add(provider);
    }

    /**
     * Registers a provider for a selection of message types.
     *
     * @param provider the provider to add
     * @param msgs     the message codes
     */
    public void addProviders(TelegramProvider provider, String... msgs) {
        for (String msg : msgs)
            addProvider(provider, msg);
    }

    /**
     * Unregister the specified listener for the specified message code.
     *
     * @param listener the listener to remove
     * @param msg      the message code
     */
    public void removeListener(Telegraph listener, String msg) {
        Array<Telegraph> listeners = msgListeners.get(msg);
        if (listeners != null) {
            listeners.removeValue(listener, true);
        }
    }

    /**
     * Unregister the specified listener for the selection of message codes.
     *
     * @param listener the listener to remove
     * @param msgs     the message codes
     */
    public void removeListener(Telegraph listener, String... msgs) {
        for (String msg : msgs)
            removeListener(listener, msg);
    }

    /**
     * Unregisters all the listeners for the specified message code.
     *
     * @param msg the message code
     */
    public void clearListeners(String msg) {
        msgListeners.remove(msg);
    }

    /**
     * Unregisters all the listeners for the given message codes.
     *
     * @param msgs the message codes
     */
    public void clearListeners(String... msgs) {
        for (String msg : msgs)
            clearListeners(msg);
    }

    /**
     * Removes all the registered listeners for all the message codes.
     */
    public void clearListeners() {
        msgListeners.clear();
    }

    /**
     * Unregisters all the providers for the specified message code.
     *
     * @param msg the message code
     */
    public void clearProviders(String msg) {
        msgProviders.remove(msg);
    }

    /**
     * Unregisters all the providers for the given message codes.
     *
     * @param msgs the message codes
     */
    public void clearProviders(String... msgs) {
        for (String msg : msgs)
            clearProviders(msg);
    }

    /**
     * Removes all the registered providers for all the message codes.
     */
    public void clearProviders() {
        msgProviders.clear();
    }

    /**
     * Removes all the telegrams from the queue and releases them to the internal pool.
     */
    public void clearQueue() {
        for (int i = 0; i < queue.size(); i++) {
            POOL.free(queue.get(i));
        }
        queue.clear();
    }

    /**
     * Removes all the telegrams from the queue and the registered listeners for all the messages.
     */
    public void clear() {
        clearQueue();
        clearListeners();
        clearProviders();
    }

    /**
     * Sends an immediate message to all registered listeners, with no extra info.
     * <p>
     * This is a shortcut method for {@link #dispatchMessage(float, Telegraph, Telegraph, String, Object, boolean) dispatchMessage(0,
     * null, null, msg, null, false)}
     *
     * @param msg the message code
     */
    public void dispatchMessage(String msg) {
        dispatchMessage(0f, null, null, msg, null, false);
    }
//
//    /**
//     * Sends an immediate message to all registered listeners, with no extra info.
//     * <p>
//     * This is a shortcut method for {@link #dispatchMessage(float, Telegraph, Telegraph, String, Object, boolean) dispatchMessage(0,
//     * sender, null, msg, null, false)}
//     *
//     * @param receiver the receiver of the telegram
//     * @param msg      the message code
//     */
//    public void dispatchMessage(Telegraph receiver, String msg) {
//        dispatchMessage(0f, null, receiver, msg, null, false);
//    }

    public void dispatchMessage(String msg, Telegraph... receivers) {
        for (Telegraph receiver : receivers) {
            dispatchMessage(0f, null, receiver, msg, null, false);
        }
    }

    public void dispatchMessage(String msg, Object extraInfo, Telegraph... receivers) {
        for (Telegraph receiver : receivers) {
            dispatchMessage(0f, null, receiver, msg, extraInfo, false);
        }
    }

    public void dispatchMessage(Telegraph sender, String msg, Telegraph... receivers) {
        for (Telegraph receiver : receivers) {
            dispatchMessage(0f, sender, receiver, msg, null, false);
        }
    }

//    public void dispatchMessage(Telegraph sender, Telegraph receiver, String msg) {
//        dispatchMessage(0f, sender, receiver, msg, null, false);
//    }


    public void dispatchMessage(Telegraph sender, String msg, Telegraph receiver, Object extraInfo) {
        dispatchMessage(0f, sender, receiver, msg, extraInfo, false);
    }

//    public void dispatchMessage(Telegraph sender, Telegraph receiver, String msg, Object extraInfo, boolean needsReturnReceipt) {
//        dispatchMessage(0f, sender, receiver, msg, extraInfo, needsReturnReceipt);
//    }
//
//    public void dispatchMessage(float delay, String msg) {
//        dispatchMessage(delay, null, null, msg, null, false);
//    }

    public void dispatchMessage(float delay, String msg, Telegraph... receivers) {
        for (Telegraph receiver : receivers) {
            dispatchMessage(delay, null, receiver, msg, null, false);
        }
    }

//    public void dispatchMessage(Telegraph sender, float delay, String msg, Telegraph... receivers) {
//        for (Telegraph receiver : receivers) {
//            dispatchMessage(delay, sender, receiver, msg, null, false);
//        }
//    }

    public void dispatchMessage(Telegraph sender, float delay, Telegraph receiver, String msg, Object extraInfo) {
        dispatchMessage(delay, sender, receiver, msg, extraInfo, false);
    }

    public void queueMessage(String msg, Telegraph... receivers) {
        for (Telegraph receiver : receivers) {
            queueMessage(0f, null, receiver, msg, null, false);
        }
    }

    public void queueMessage(String msg, Object extraInfo, Telegraph... receivers) {
        for (Telegraph receiver : receivers) {
            queueMessage(0f, null, receiver, msg, extraInfo, false);
        }
    }

    public void queueMessage(float delay, String msg, Telegraph... receivers) {
        for (Telegraph receiver : receivers) {
            queueMessage(delay, null, receiver, msg, null, false);
        }
    }

    public void queueMessage(float delay, String msg, Object extraInfo, Telegraph... receivers) {
        for (Telegraph receiver : receivers) {
            queueMessage(delay, null, receiver, msg, extraInfo, false);
        }
    }

    public void queueMessage(float delay, Telegraph sender, Telegraph receiver, String msg, Object extraInfo,
                             boolean needsReturnReceipt) {
//        new RuntimeException(msg).printStackTrace();
        if (sender == null && needsReturnReceipt)
            throw new IllegalArgumentException("Sender cannot be null when a return receipt is needed");

        // Get a telegram from the pool
        Telegram telegram = POOL.obtain();
        telegram.sender = sender;
        telegram.receiver = receiver;
        telegram.message = msg;
        telegram.extraInfo = extraInfo;
        telegram.returnReceiptStatus = needsReturnReceipt ? Telegram.RETURN_RECEIPT_NEEDED : Telegram.RETURN_RECEIPT_UNNEEDED;
        telegram.setTimestamp(delay);
        telegram.queueIndex = ++queueIndex;
        // Put the telegram in the queue
        boolean added = queue.add(telegram);
        // Return it to the pool if has been rejected
        if (!added) POOL.free(telegram);
    }

    public boolean isQueueEmpty() {
        return queue.size() <= 0;
    }

    public void removeMessage(String msg) {
    }

    /**
     * Given a message, a receiver, a sender and any time delay, this method routes the message to the correct agents (if no delay)
     * or stores in the message queue to be dispatched at the correct time.
     *
     * @param delay              the delay in seconds
     * @param sender             the sender of the telegram
     * @param receiver           the receiver of the telegram; if it's {@code null} the telegram is broadcasted to all the receivers
     *                           registered for the specified message code
     * @param msg                the message code
     * @param extraInfo          an optional object
     * @param needsReturnReceipt whether the return receipt is needed or not
     * @throws IllegalArgumentException if the sender is {@code null} and the return receipt is needed
     */
    public void dispatchMessage(float delay, Telegraph sender, Telegraph receiver, String msg, Object extraInfo,
                                boolean needsReturnReceipt) {
        if (sender == null && needsReturnReceipt)
            throw new IllegalArgumentException("Sender cannot be null when a return receipt is needed");

        // Get a telegram from the pool
        Telegram telegram = POOL.obtain();
        telegram.sender = sender;
        telegram.receiver = receiver;
        telegram.message = msg;
        telegram.extraInfo = extraInfo;
        telegram.returnReceiptStatus = needsReturnReceipt ? Telegram.RETURN_RECEIPT_NEEDED : Telegram.RETURN_RECEIPT_UNNEEDED;

        // If there is no delay, route telegram immediately
        if (delay <= 0.0f) {

            // TODO: should we set the timestamp here?
            // telegram.setTimestamp(GdxAI.getTimepiece().getTime());

            if (debugEnabled) {
//				float currentTime = GdxAI.getTimepiece().getTime();
                Gdx.app.log(LOG_TAG,
                        "Instant telegram dispatched at time: " + 0 + " by " + getTelegraphName(sender) + " for " + getTelegraphName(
                                receiver) + ". Message code is " + msg);
            }

            // Send the telegram to the recipient
            discharge(telegram);
        } else {
//			float currentTime = GdxAI.getTimepiece().getTime();

            // Set the timestamp for the delayed telegram
            telegram.setTimestamp(delay);

            // Put the telegram in the queue
            boolean added = queue.add(telegram);

            // Return it to the pool if has been rejected
            if (!added) POOL.free(telegram);

            if (debugEnabled) {
                if (added)
                    Gdx.app.log(LOG_TAG,
                            "Delayed telegram from " + getTelegraphName(sender) + " for " + getTelegraphName(receiver)
                                    + " recorded at time " + 0 + ". Message code is " + msg);
                else
                    Gdx.app.log(LOG_TAG,
                            "Delayed telegram from " + getTelegraphName(sender) + " for " + getTelegraphName(receiver)
                                    + " rejected by the queue. Message code is " + msg);
            }
        }
    }

    public String getTelegraphName(Telegraph telegraph) {
        if (telegraph == null)
            return "NULL";
        else
            return telegraph.toString().split("\n")[0];
    }

    /**
     * Dispatches any delayed telegrams with a timestamp that has expired. Dispatched telegrams are removed from the queue.
     * <p>
     * This method must be called regularly from inside the main game loop to facilitate the correct and timely dispatch of any
     * delayed messages. Notice that the message dispatcher internally calls {@link #()
     * GdxAI.getTimepiece().getTime()} to get the current AI time and properly dispatch delayed messages. This means that
     * <ul>
     * <li>if you forget to {@link #(float) update the timepiece} the delayed messages won't be dispatched.</li>
     * <li>ideally the timepiece should be updated before the message dispatcher.</li>
     * </ul>
     */
    public void update(float delta) {
//		float currentTime = GdxAI.getTimepiece().getTime();

        // Peek at the queue to see if any telegrams need dispatching.
        // Remove all telegrams from the front of the queue that have gone
        // past their time stamp.
        Telegram telegram;
        while ((telegram = queue.peek()) != null) {

            // Exit loop if the telegram is in the future  处理延迟时间
            telegram.update(delta);
            if (telegram.getTimestamp() > 0) break;

            if (debugEnabled) {
                Gdx.app.log(LOG_TAG,
                        "Queued telegram ready for dispatch: Sent to " + telegram.receiver.toString().split("\n")[0] + ". Message code is " + telegram.message);
            }

            // Send the telegram to the recipient
            discharge(telegram);

            // Remove it from the queue
            queue.poll();
        }

    }

    /**
     * Scans the queue and passes pending messages to the given callback in any particular order.
     * <p>
     * Typically this method is used to save (serialize) pending messages and restore (deserialize and schedule) them back on game
     * loading.
     *
     * @param callback The callback used to report pending messages individually.
     **/
    public void scanQueue(PendingMessageCallback callback) {
//		float currentTime = GdxAI.getTimepiece().getTime();
        int queueSize = queue.size();
        for (int i = 0; i < queueSize; i++) {
            Telegram telegram = queue.get(i);
            callback.report(telegram.getTimestamp(), telegram.sender, telegram.receiver, telegram.message,
                    telegram.extraInfo, telegram.returnReceiptStatus);
        }
    }

    /**
     * This method is used by {@link #dispatchMessage(Telegraph, float, Telegraph, String, Object) dispatchMessage} for immediate
     * telegrams and {@link #update(float) update} for delayed telegrams. It first calls the message handling method of the
     * receiving agents with the specified telegram then returns the telegram to the pool.
     *
     * @param telegram the telegram to discharge
     */
    private void discharge(Telegram telegram) {
        if (telegram.receiver != null) {
            // Dispatch the telegram to the receiver specified by the telegram itself
            if (!telegram.receiver.handleMessage(telegram)) {
                // Telegram could not be handled
                if (debugEnabled)
                    Gdx.app.log(LOG_TAG, "Message " + telegram.message + " not handled");
            }
        } else {
            // Dispatch the telegram to all the registered receivers
            int handledCount = 0;
            Array<Telegraph> listeners = msgListeners.get(telegram.message);
            if (listeners != null) {
                for (int i = 0; i < listeners.size; i++) {
                    if (listeners.get(i).handleMessage(telegram)) {
                        handledCount++;
                    }
                }
            }
            // Telegram could not be handled
            if (debugEnabled && handledCount == 0)
                Gdx.app.log(LOG_TAG, "Message " + telegram.message + " not handled");
        }

        if (telegram.returnReceiptStatus == Telegram.RETURN_RECEIPT_NEEDED) {
            // Use this telegram to send the return receipt
            telegram.receiver = telegram.sender;
            telegram.sender = this;
            telegram.returnReceiptStatus = Telegram.RETURN_RECEIPT_SENT;
            discharge(telegram);
        } else {
            // Release the telegram to the pool
            POOL.free(telegram);
        }
    }

    /**
     * Handles the telegram just received. This method always returns {@code false} since usually the message dispatcher never
     * receives telegrams. Actually, the message dispatcher implements {@link Telegraph} just because it can send return receipts.
     *
     * @param msg The telegram
     * @return always {@code false}.
     */
    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    /**
     * A {@code PendingMessageCallback} is used by the {@link MessageDispatcher#scanQueue(PendingMessageCallback) scanQueue} method
     * of the {@link MessageDispatcher} to report its pending messages individually.
     *
     * @author davebaol
     */
    public interface PendingMessageCallback {

        /**
         * Reports a pending message.
         *
         * @param delay               The remaining delay in seconds
         * @param sender              The message sender
         * @param receiver            The message receiver
         * @param message             The message code
         * @param extraInfo           Any additional information that may accompany the message
         * @param returnReceiptStatus The return receipt status of the message
         */
        void report(float delay, Telegraph sender, Telegraph receiver, Object message, Object extraInfo,
                    int returnReceiptStatus);
    }

}
