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
package org.kaazing.net.impl.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

public class ResumableTimerTest {

    @Test
    public void testTaskSchedule() {
        System.out.println("***** ResumableTimerTest.testTaskSchedule() *****");
        ResumableTimer timer = new ResumableTimer(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task scheduled at " + getFormattedDateTime());
            }
        }, 5000, false);
        
        System.out.println("Starting timer at " + getFormattedDateTime());
        timer.start();

        try {
            // While the timer is scheduled, sleep for 6000ms so that the task
            // is executed before the thread wakes up.
            Thread.sleep(6000);
            assertTrue(timer.didTaskExecute());
            timer.cancel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    @Test
    public void testTimerPause() {
        System.out.println("***** ResumableTimerTest.testTimerPause() *****");
        ResumableTimer timer = new ResumableTimer(new Runnable() {
            @Override
            public void run() {
                // This should not get called as we are pausing the timer.
                System.out.println("Task scheduled at " + getFormattedDateTime());
            }
        }, 5000, false);
        
        System.out.println("Starting timer at " + getFormattedDateTime());
        timer.start();

        try {
            // While the timer is scheduled, sleep for 2000ms.
            Thread.sleep(2000);
            
            timer.pause();

            System.out.println("Timer paused at " + getFormattedDateTime());
            System.out.println("Now timer delay is " + timer.getDelay());

            // Ensure that the original delay of 5000 is still the same as
            // we had selected DO_NOT_UPDATE_DELAY as the strategy.
            assertTrue(timer.getDelay() == 5000);
            
            // Since the timer was paused, the task should NOT have executed.
            assertFalse(timer.didTaskExecute());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    @Test
    public void testTimerResume() {
        System.out.println("***** ResumableTimerTest.testTimerResume() *****");
        final ResumableTimer timer = new ResumableTimer(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task scheduled at " + getFormattedDateTime());
            }
        }, 5000, true);
        
        System.out.println("Starting timer at " + getFormattedDateTime());
        timer.start();

        try {
            // While the timer is scheduled, sleep for 2000ms.
            Thread.sleep(2000);
            
            timer.pause();

            System.out.println("Timer paused at " + getFormattedDateTime());
            System.out.println("Now timer delay is " + timer.getDelay());
            
            // Ensure that the original delay of 5000 is now reduced.
            assertTrue(timer.getDelay() < 5000);
            
            // Since the timer was paused, the task should NOT have executed.
            assertFalse(timer.didTaskExecute());
            
            Thread.sleep(1000);
            
            // Resume the timer with the remaining delay.
            timer.resume();
            
            System.out.println("Timer resumed at " + getFormattedDateTime());
            System.out.println("Timer delay is " + timer.getDelay());
            
            // Now that the timer has been resumed, sleep for 4000ms for the 
            // task to get executed.
            Thread.sleep(4000);

            // By now the task should have been executed.
            assertTrue(timer.didTaskExecute());
            timer.cancel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    @Test @Ignore
    public void testTimerPauseResume() {
        System.out.println("***** ResumableTimerTest.testTimerPauseResume() *****");
        final ResumableTimer timer = new ResumableTimer(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task scheduled at " + getFormattedDateTime());
            }
        }, 5000, true);
        
        System.out.println("Starting timer at " + getFormattedDateTime());
        timer.start();

        try {
            Thread.sleep(500);

            Thread pauseTimerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(100);
                            if (timer.didTaskExecute()) {
                                break;
                            }
                            timer.pause();
                            // System.out.println("Paused at  " + getFormattedDateTime() + ";   delay = " + timer.getDelay());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("PauseTimerThread is exiting at " + getFormattedDateTime());
                }
            }, "PauseTimerThread");
            pauseTimerThread.start();

            Thread.sleep(500);

            Thread resumeTimerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(100);
                            if (timer.didTaskExecute()) {
                                break;
                            }
                            timer.resume();
                            // System.out.println("Resumed at " + getFormattedDateTime() + ";   delay = " + timer.getDelay());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("ResumeTimerThread is exiting at " + getFormattedDateTime());
                }
            }, "ResumeTimerThread");
            resumeTimerThread.start();

            // Sleep for 15000ms for the task to get executed.
            Thread.sleep(15000);

            // System.out.println("End of test " + getFormattedDateTime());

            // By now the task should have been executed.
            assertTrue(timer.didTaskExecute());
            timer.cancel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    
    private String getFormattedDateTime() {
        Date myDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
        return sdf.format(myDate);
    }
}
