package com.robertoguijon.login.util.events

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

//Clase que nos permite que los MutableLiveData puedan ser observados solo una vez
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        // Solo notifica al observer si el valor ha cambiado y no ha sido consumido
        super.observe(owner, { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    // Utilidad para llamar al evento sin valor específico
    fun call() {
        value = null
    }
}
