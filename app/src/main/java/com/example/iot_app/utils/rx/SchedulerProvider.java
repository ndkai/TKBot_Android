package com.example.iot_app.utils.rx;

import io.reactivex.Scheduler;

public interface SchedulerProvider {
    Scheduler computation();
    Scheduler io();
    Scheduler ui();
}
