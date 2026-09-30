package com.opera.firstapp.viewModel
import android.R
import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.opera.firstapp.models.User
import com.opera.firstapp.navigation.ROUTE_DASHBOARD
import com.opera.firstapp.navigation.ROUTE_LOGIN
import com.opera.firstapp.navigation.ROUTE_REGISTER
import com.opera.firstapp.navigation.ROUTE_USERDASHBOARD


class AuthViewModel (var navController: NavHostController, var context: Context){
        var mAuth=FirebaseAuth.getInstance()
    //register  function to create new users
    fun signup(fullname: String,email: String,password: String,confirmpass: String){
        //validation
        if( email.isBlank()|| password.isBlank() || confirmpass.isBlank()){
            Toast.makeText(context,"Email and password cannot be blank",
                Toast.LENGTH_LONG).show()
            return
        }else if( password!=confirmpass){
            Toast.makeText(context,"password and confirm passsword do not match",
                Toast.LENGTH_LONG).show()
        }else{
            //create user
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userdata= User(fullname,email,password,mAuth.currentUser!!.uid,"user")
                        //save userdata in realtime database
                        val regRef= FirebaseDatabase.getInstance().
                        getReference().child("Users/" +mAuth.currentUser!!.uid)
                        regRef.setValue(userdata).addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(context,"user registered successfully", Toast.LENGTH_LONG).show()
                                //navigate to login
                                navController.navigate(ROUTE_LOGIN)
                            }else{
                                Toast.makeText(context,"${it.exception!!.message}", Toast.LENGTH_LONG).show()
                                navController.navigate(ROUTE_REGISTER)
                            }
                        }

                    }
                    else{
                        navController.navigate(ROUTE_REGISTER)

                    }
                }

        }

    }
    //login function
    fun login(email: String,password: String){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                val userId=mAuth.currentUser?.uid
                //fetch user role
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(userId!!).get().addOnSuccessListener { snapshot ->
                        var role = snapshot.child("role").value.toString()
                        //role based navigation
                        if (role == "admin") {
                            navController.navigate(ROUTE_DASHBOARD)
                        } else {
                            navController.navigate(ROUTE_USERDASHBOARD)
                        }
                        Toast.makeText(context, "Successfully  login in", Toast.LENGTH_LONG).show()
                    }
            }else{
                Toast.makeText(context,it.exception?.message ?:"error logging in", Toast.LENGTH_LONG).show()

            }
        }

    }
    //signout function
    fun signout(){
            mAuth.signOut()
            navController.navigate(ROUTE_LOGIN)
            { popUpTo(0) }

    }
    //get current username function
    fun getCurrentUserName(onResult:(String) ->Unit){
        val userId=mAuth.currentUser?.uid
        if (userId==null) {
            onResult("user")
            return
        }
        FirebaseDatabase.getInstance().getReference("Users")
            .child(userId)
            .get()
            .addOnSuccessListener{snapshot ->
                val fullname=snapshot.child("fullname").getValue(String::class.java)
                onResult(fullname ?:"user")

            }
    }
}