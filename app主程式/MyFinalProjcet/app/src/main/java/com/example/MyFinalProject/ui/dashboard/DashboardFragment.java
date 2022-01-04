package com.example.MyFinalProject.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.MyFinalProject.R;
import com.example.MyFinalProject.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = getActivity().getWindow();
            /*
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            */


        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button login_btn = view.findViewById(R.id.login_btn);
        //updateUi(0);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog();
            }
        });

        Button signup_btn = view.findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupDialog();
            }
        });

        CardView cardView = view.findViewById(R.id.logout_btn);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        updateUi();
    }

    public void updateUi(){

            FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            ImageView imageview= (ImageView)getView().findViewById(R.id.profile_img);
            imageview.setImageDrawable(getResources().getDrawable(R.drawable.profile));
            TextView textView_userAccount =getView().findViewById(R.id.user_account);
            textView_userAccount.setText(user.getEmail());
            LinearLayout profile = (LinearLayout) getView().findViewById(R.id.profile_data);
            profile.setVisibility(View.VISIBLE);
            LinearLayout login_opt = (LinearLayout) getView().findViewById(R.id.login_opt);
            login_opt.setVisibility(View.GONE);
        }else{
            //No User is Logged in
            ImageView imageview= (ImageView)getView().findViewById(R.id.profile_img);
            imageview.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_account_circle_24));

            LinearLayout profile = (LinearLayout) getView().findViewById(R.id.profile_data);
            profile.setVisibility(View.GONE);
            LinearLayout login_opt = (LinearLayout) getView().findViewById(R.id.login_opt);
            login_opt.setVisibility(View.VISIBLE);
        }


    }

    private void loginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.login_layout, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TextInputLayout email_et = view.findViewById(R.id.email);
                        TextInputLayout password_et = view.findViewById(R.id.password);
                        String email = email_et.getEditText().getText().toString().trim();
                        String password = password_et.getEditText().getText().toString().trim();
                        signUser(email, password);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void signupDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.login_layout, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TextInputLayout email_et = view.findViewById(R.id.email);
                        TextInputLayout password_et = view.findViewById(R.id.password);
                        String email = email_et.getEditText().getText().toString().trim();
                        String password = password_et.getEditText().getText().toString().trim();
                        //Authentication authentication = new Authentication();
                        //authentication.createUser(email, password);
                        Log.e("TAG", email + password);
                        updateUi();
                        createUser(email, password);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("User_status", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUi();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("User_status", "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            updateUi();
                        }
                    }
                });
    }

    public void signUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUi();
                        } else {
                            updateUi();
                        }
                    }
                });
    }
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        updateUi();
    }


}