package tn.esprit.green_world

import android.app.Application
import com.stripe.android.PaymentConfiguration

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51ODOR4KuYIHrh7zwIzoFHvpAv7c8hMVDcUZXdRjJD53dmlmUA11W8aJmWotozC6D4PepaWPkuu6aYP5C0rOg3iXz002HAs7ygC"
        )

    }
}