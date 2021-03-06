package com.example.login_screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*

class Login : AppCompatActivity() {

    var phone_No: TextInputLayout? = null
    var password: TextInputLayout? = null
    var progressBar: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        phone_No = findViewById((R.id.login_phone_number))
        password = findViewById((R.id.login_password))
        progressBar = findViewById((R.id.login_progress_bar))

    }


    fun callForgetPassword(view: View) {
        val intent = Intent(applicationContext, ForgetPassword::class.java)

        startActivity(intent)
    }


//    fun letTheUserLoggedIn(view: View?) {
//    }
//
//
//        public void callLOginScreen(View View) {
//            Intent intent = new Intent(getApplicationContext(), Login.class);
//            Pair[] pairs = new Pair[1];
//            pairs[0] = new Pair<android.view.View, String>(findViewById(R.id.letTheUserLogIn), "transition_Login");
//
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
//            startActivity(intent, options.toBundle());
//        }


    fun letTheUserLoggedIn(view: View?) {
//        if (!isConnected(this)) {
//            showCustomDialog()
//        }

        if (!validateFields()) {
            return
        }
        progressBar!!.visibility = View.VISIBLE

        var _id: Int = 0;
        var _phone = phone_No!!.editText!!.text.toString().trim { it <= ' ' }
        if (_phone.get(0) == '0') {
            _phone = _phone.substring(1)
        }
        _phone = "+92" + _phone

        var _password = password!!.editText!!.text.toString().trim { it <= ' ' }


        //DatabaseQuery
        val checkUser: Query =
            FirebaseDatabase.getInstance().getReference("Students").orderByChild("phone_No")
                .equalTo(_phone)
        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    phone_No!!.error = null
                    phone_No!!.isErrorEnabled
                    var systemPassword = snapshot.child(_phone).child("password").getValue(
                        String::class.java)

                    if (_password.equals(systemPassword)) {
                        progressBar!!.visibility = View.VISIBLE
                        password!!.isErrorEnabled
                        password!!.error = null
                        val name = snapshot.child(_phone).child("fullName").getValue(
                            String::class.java
                        )
                        Toast.makeText(this@Login, "Welocome $name", Toast.LENGTH_SHORT).show();

                    } else {

                        progressBar!!.visibility = View.GONE
                        Toast.makeText(this@Login, "Wrong Password", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    progressBar!!.visibility = View.GONE
                    Toast.makeText(this@Login, "Wrong Credentials", Toast.LENGTH_SHORT).show();

                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar!!.visibility = View.GONE
                Toast.makeText(this@Login, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
    }


//    private fun showCustomDialog() {
//        val builder = android.app.AlertDialog.Builder(this@Login)
//        builder.setMessage("Please Connect to the Internet to Proceed Further!!!")
//            .setCancelable(false)
//            .setPositiveButton(
//                " Connect"
//            ) { dialog, which -> startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }
//            .setNegativeButton(
//                "Cancel"
//            ) { dialog, which ->
//                val intent = Intent(this@Login, MainActivity::class.java)
//                startActivity(intent)
//            }
//    }


//    private fun isConnected(login: Login): Boolean {
//        var connectivityManager: ConnectivityManager =
//            login.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        var wifiConn: NetworkInfo? =
//            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//        var mobConn: NetworkInfo? =
//            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//
//        if ((wifiConn != null && wifiConn.isConnected()) || (mobConn != null && mobConn.isConnected())) {
//            return true
//        } else {
//
//            return false
//        }
//    }


    fun callSignUpFromLogin(view: View) {
        val intent = Intent(applicationContext, Signup::class.java)

        startActivity(intent)
    }

    private fun validateFields(): Boolean {
        val _phone = phone_No!!.editText!!.text.toString().trim { it <= ' ' }
        val _password = password!!.editText!!.text.toString().trim { it <= ' ' }
        return if (_phone.isEmpty()) {
            phone_No!!.error = "Phone No can not be empty!"
            phone_No!!.requestFocus()
            false
        } else if (_password.isEmpty()) {
            password!!.error = "Password can not be empty!"
            password!!.requestFocus()
            false
        } else {
            phone_No!!.error = null
            phone_No!!.isErrorEnabled = false
            password!!.error = null
            password!!.isErrorEnabled = false
            true
        }
    }

    fun callBack(view: View) {
        val intent = Intent(applicationContext, MainActivity::class.java)

        startActivity(intent)
    }
}