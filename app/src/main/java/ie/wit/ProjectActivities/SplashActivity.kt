                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        //4second splash time
        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this@SplashScreenActivity, Login::class.java))
            //finish this activity
            finish()
        },4000)

    }
}