package com.pet.adoption;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;
import android.webkit.MimeTypeMap;
import android.widget.*;
import android.view.*;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class fragment_add extends Fragment {

    private RoundedImageView image, editImg;
    private ImageView CalendarBtn, cmrImg;
    private TextView imageTxt, dateTxt;
    private AutoCompleteTextView autoCompleteTextView, autoCompleteTextView1;
    private Button uploadNewPet;
    private EditText nameTxt, weightTxt, descriptionTxt, cityTxt;
    private View view;
    private String spinner1, spinner2, radioButtonchecked1, Owner;
    private Uri uri;
    private int count = 0;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pet-adoption-c09f7-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);

        SharedPreferences sp1=getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        Owner =sp1.getString("username", "");

        nameTxt = view.findViewById(R.id.nameTxt);
        cityTxt = view.findViewById(R.id.cityTxt);
        weightTxt = view.findViewById(R.id.weightTxt);
        descriptionTxt = view.findViewById(R.id.descriptionTxt);
        dateTxt = view.findViewById(R.id.dateTxt);
        CalendarBtn = view.findViewById(R.id.CalendarBtn);

        CalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        autoCompleteTextView = view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView1 = view.findViewById(R.id.spinnerRace);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextView1.setAdapter(adapter1);

        image = view.findViewById(R.id.imgPicker);
        editImg = view.findViewById(R.id.editImg);
        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(fragment_add.this)
                        .crop()
                        .start();
            }
        });
        uploadNewPet = view.findViewById(R.id.uploadNewPet);
        uploadNewPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                final RadioGroup radioGrp1 = view.findViewById(R.id.radioBtnGrp);
                int radioId1 = radioGrp1.getCheckedRadioButtonId();
                RadioButton radioButton1 = view.findViewById(radioId1);
                radioButtonchecked1 = radioButton1.getText().toString();
                uploadDataToFirebase(uri);
                }
        });

        cmrImg = view.findViewById(R.id.cmrImg);
        imageTxt = view.findViewById(R.id.imgTxt);
        imageTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(fragment_add.this)
                        .crop()
                        .start();
            }
        });

        return view;
    }

    private void showDatePicker() {
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date").build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate  = format.format(calendar.getTime());

                dateTxt.setText(""+formattedDate);
            }
        });
        materialDatePicker.show(getFragmentManager(), "TAG");
    }

    private void uploadDataToFirebase(Uri uri) {
        String spinner1 = autoCompleteTextView.getText().toString();
        String spinner2 = autoCompleteTextView1.getText().toString();
        final String weightTxt1 = weightTxt.getText().toString();
        final String descriptionTxt1 = descriptionTxt.getText().toString();
        final String nameTxt1 = nameTxt.getText().toString();
        final String dateTxt1 = dateTxt.getText().toString();
        final String cityTxt1 = cityTxt.getText().toString();
        final String uniqueID = UUID.randomUUID().toString();
        if (dateTxt1.equals("Select Date") || count == 0 ||  cityTxt1.isEmpty() || spinner1.isEmpty() || spinner2.isEmpty() || weightTxt1.isEmpty() || descriptionTxt1.isEmpty())
            Toast.makeText(getContext(), "We need more data!", Toast.LENGTH_SHORT).show();
        else {
            StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("pets").child(uniqueID).child("ID").setValue(uniqueID);
                            databaseReference.child("pets").child(uniqueID).child("Image").setValue(uri.toString());
                            databaseReference.child("pets").child(uniqueID).child("PetName").setValue(nameTxt1);
                            databaseReference.child("pets").child(uniqueID).child("PetType").setValue(spinner1);
                            databaseReference.child("pets").child(uniqueID).child("PetBreed").setValue(spinner2);
                            databaseReference.child("pets").child(uniqueID).child("Weight").setValue(weightTxt1);
                            databaseReference.child("pets").child(uniqueID).child("Description").setValue(descriptionTxt1);
                            databaseReference.child("pets").child(uniqueID).child("Gender").setValue(radioButtonchecked1);
                            databaseReference.child("pets").child(uniqueID).child("BirthDay").setValue(dateTxt1);
                            databaseReference.child("pets").child(uniqueID).child("Owner").setValue(Owner);
                            databaseReference.child("pets").child(uniqueID).child("City").setValue(cityTxt1);
                            Toasty.success(getContext(), "Added successfully", Toast.LENGTH_SHORT, true).show();
                            cleanAll();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            uri = data.getData();
            image.setImageURI(uri);
            imageTxt.setVisibility(View.GONE);
            editImg.setVisibility(View.VISIBLE);
            cmrImg.setVisibility(View.GONE);
            count = 1;
        }
    }

    private void cleanAll(){
        imageTxt.setVisibility(View.VISIBLE);
        image.setImageResource(R.drawable.round_dark_back_bleu5);
        nameTxt.getText().clear();
        descriptionTxt.getText().clear();
        weightTxt.getText().clear();
        dateTxt.setText("Select Date");
        autoCompleteTextView.setText("");
        autoCompleteTextView1.setText("");
    }

}