package com.example.itisconnect.views;

import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.itisconnect.R;
import com.example.itisconnect.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddNewUserActivity extends AppCompatActivity
{

    private Spinner sexSpinner;
    private Spinner departmentSpinner;
    private Spinner roleAdminSpinner;
    private Button saveButton, cancelButton;
    private ImageView userAvatar, addPhotoButton;
    private EditText userEmail, userPassword, userFullName, userBirthDate, userPlaceOfOrigin, userPhoneNumber, userCohort;
    private CheckBox secretary, viceSecretary, UVBCH, head, viceHead, member;
    private ProgressBar progressBar;
    private ActivityResultLauncher<String> mTakePhoto;
    private Uri imageUri;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private CollectionReference userRef = db.collection("Users");
    private FirebaseAuth fAuth;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        storageRef = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        sexSpinner = findViewById(R.id.sex_spinner);
        departmentSpinner = findViewById(R.id.department_spinner);
        roleAdminSpinner = findViewById(R.id.role_admin_spinner);

        progressBar = findViewById(R.id.progress_bar_user_add_view);

        userAvatar = findViewById(R.id.user_image_add_view);
        addPhotoButton = findViewById(R.id.user_camera_button_add_view);

        saveButton = findViewById(R.id.user_save_button);
        cancelButton = findViewById(R.id.user_cancel_button);

        userAvatar = findViewById(R.id.user_image_add_view);
        addPhotoButton = findViewById(R.id.user_camera_button_add_view);

        userEmail = findViewById(R.id.user_email_add_view);
        userPassword = findViewById(R.id.user_password_add_view);
        userFullName = findViewById(R.id.user_fullname_add_view);
        userBirthDate = findViewById(R.id.user_birthdate_add_view);
        userPlaceOfOrigin = findViewById(R.id.user_placeoforigin_add_view);
        userPhoneNumber = findViewById(R.id.user_phonenumber_add_view);
        userCohort = findViewById(R.id.user_cohort_add_view);

        secretary = findViewById(R.id.position_secretary_checkbox);
        viceSecretary = findViewById(R.id.position_vice_secretary_checkbox);
        UVBCH = findViewById(R.id.position_UVBCH_checkbox);
        head = findViewById(R.id.position_head_checkbox);
        viceHead = findViewById(R.id.position_deputy_checkbox);
        member = findViewById(R.id.position_member_checkbox);

        mTakePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>()
        {
            @Override
            public void onActivityResult(Uri o)
            {
                userAvatar.setImageURI(o);
                imageUri = o;
            }
        });

        addPhotoButton.setOnClickListener(v ->
        {
            mTakePhoto.launch("image/*");
        });

        cancelButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveUser());

        setupAdapters();
    }

    private void setupAdapters()
    {
        ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(this,
                R.array.sex_options, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(sexAdapter);

        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource(this,
                R.array.department_options, android.R.layout.simple_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);

        ArrayAdapter<CharSequence> roleAdminAdapter = ArrayAdapter.createFromResource(this,
                R.array.role_admin_options, android.R.layout.simple_spinner_item);
        roleAdminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleAdminSpinner.setAdapter(roleAdminAdapter);
    }

    private void saveUser()
    {
        String fullName = userFullName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        String birthDate = userBirthDate.getText().toString().trim();
        String placeOfOrigin = userPlaceOfOrigin.getText().toString().trim();
        String phoneNumber = userPhoneNumber.getText().toString().trim();
        String cohort = userCohort.getText().toString().trim();
        String sex = sexSpinner.getSelectedItem().toString();
        String department = departmentSpinner.getSelectedItem().toString();
        boolean roleAdmin = roleAdminSpinner.getSelectedItem().toString().equals("Admin");
        String userPosition = "";
        int priority = 5;

        progressBar.setVisibility(View.VISIBLE);

        if (secretary.isChecked())
        {
            userPosition = "Bí thư";
            priority = Math.min(priority, 0);
        }
        if (viceSecretary.isChecked())
        {
            userPosition = "Phó Bí thư";
            priority = Math.min(priority, 1);
        }
        if (UVBCH.isChecked())
        {
            userPosition = "UVBCH";
            priority = Math.min(priority, 2);
        }
        if (head.isChecked())
        {
            priority = Math.min(priority, 3);
            if (!userPosition.isEmpty())
            {
                userPosition += ", Trưởng ban " + department;
            }
            else
            {
                userPosition = "Trưởng ban " + department;
            }
        }
        if (viceHead.isChecked())
        {
            priority = Math.min(priority, 4);
            if (!userPosition.isEmpty())
            {
                userPosition += ", Phó ban " + department;
            }
            else
            {
                userPosition = "Phó ban " + department;
            }
        }
        if (member.isChecked())
        {
            userPosition = "Thành viên ban " + department;
            priority = Math.min(priority, 5);
        }

        if (email.isEmpty())
        {
            userEmail.setError("Không được để trống email!");
            userEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            userEmail.setError("Email không hợp lệ!");
            userEmail.requestFocus();
            return;
        }

        String birthDatePattern = "^([0-2][0-9]|(3)[0-1])/((0)[0-9]|(1)[0-2])/((19|20)\\d\\d)$";
        if (birthDate.isEmpty())
        {
            userBirthDate.setError("Ngày sinh không được để trống!");
            userBirthDate.requestFocus();
            return;
        }
        if (!birthDate.matches(birthDatePattern))
        {
            userBirthDate.setError("Ngày sinh không hợp lệ! (dd/mm/yyyy)");
            userBirthDate.requestFocus();
            return;
        }

        String phonePattern = "^0[0-9]{9}$";
        if (phoneNumber.isEmpty())
        {
            userPhoneNumber.setError("Không được để trống số điện thoại!");
            userPhoneNumber.requestFocus();
            return;
        }
        if (!phoneNumber.matches(phonePattern))
        {
            userPhoneNumber.setError("Số điện thoại không hợp lệ!");
            userPhoneNumber.requestFocus();
            return;
        }

        if (fullName.isEmpty())
        {
            userFullName.setError("Không được để trống họ tên!");
            userFullName.requestFocus();
            return;
        }

        if (placeOfOrigin.isEmpty())
        {
            userPlaceOfOrigin.setError("Không được để trống quê quán!");
            userPlaceOfOrigin.requestFocus();
            return;
        }

        if (cohort.isEmpty())
        {
            userCohort.setError("Không được để trống khóa!");
            userCohort.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6)
        {
            userPassword.setError("Mật khẩu phải có ít nhất 6 ký tự!");
            userPassword.requestFocus();
            return;
        }

        final StorageReference filepath = storageRef.child("user_avatars").child("user_avatar_" + System.currentTimeMillis());

        String finalUserPosition = userPosition;
        int finalPriority = priority;
        filepath.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
        {
            filepath.getDownloadUrl().addOnSuccessListener(uri ->
            {
                String imageUrl = uri.toString();

                User user = new User(email, fullName, birthDate, placeOfOrigin, sex, phoneNumber, cohort, department, finalUserPosition, imageUrl, roleAdmin, finalPriority);

                fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult ->
                {
                    userRef.add(user).addOnSuccessListener(documentReference ->
                    {
                        fAuth.updateCurrentUser(currentUser).addOnCompleteListener(task ->
                        {
                            Toast.makeText(this, "Thêm thành viên " + fullName + " thành công!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        }).addOnFailureListener(e ->
                        {
                            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        });

                    }).addOnFailureListener(e ->
                    {
                        Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    });
                }).addOnFailureListener(e ->
                {
                    Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                });
            });
        });
    }
}
