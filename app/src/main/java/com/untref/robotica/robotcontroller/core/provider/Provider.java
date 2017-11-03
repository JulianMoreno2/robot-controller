package com.untref.robotica.robotcontroller.core.provider;

import io.reactivex.subjects.PublishSubject;

import static com.untref.robotica.robotcontroller.core.util.InstanceCache.getInstanceFor;

public class Provider {
    public static PublishSubject<String> provideBluetoothReaderPublishSubject() {
        return getInstanceFor(PublishSubject.class, PublishSubject::create);
    }
}
