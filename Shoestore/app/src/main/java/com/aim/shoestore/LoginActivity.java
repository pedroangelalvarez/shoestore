package com.aim.shoestore;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.speech.tts.TextToSpeech;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.android.volley.RequestQueue;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int RECOGNIZE_USER = 1;
    private static final int RECOGNIZE_PASS = 2;
    private static final String TAG = MainActivity.class.getName();
    TextToSpeech t1;
    private static final int REQUEST_READ_CONTACTS = 0;

    // Creating Volley RequestQueue.
    RequestQueue requestQueue;


    //String ServerResponse = null ;

    String TempServerResponseMatchedValue = "Data Matched";
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private int CODE=0;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        insertSomeUsers();
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Locale loc = new Locale("es", "", "");
                if (t1.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE) {
                    t1.setLanguage(loc);

                }
            }
        });
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingrese su nombre de usuario", TextToSpeech.QUEUE_FLUSH, null);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                Intent intentActionRecognizeSpeech = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // Configura el Lenguaje (Español-México)
                intentActionRecognizeSpeech.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-PE");
                try {
                    startActivityForResult(intentActionRecognizeSpeech, RECOGNIZE_USER);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Tú dispositivo no soporta el reconocimiento por voz",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        mPasswordView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Ingrese su contraseña", TextToSpeech.QUEUE_FLUSH, null);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                Intent intentActionRecognizeSpeech = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // Configura el Lenguaje (Español-México)
                intentActionRecognizeSpeech.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-PE");
                try {
                    startActivityForResult(intentActionRecognizeSpeech, RECOGNIZE_PASS);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Tú dispositivo no soporta el reconocimiento por voz",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Iniciar Sesión", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        Button mRecovery = (Button) findViewById(R.id.recovery);
        mRecovery.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                t1.speak("Olvidé mi contraseña", TextToSpeech.QUEUE_FLUSH, null);
                return false;
            }
        });
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mRecovery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Recovery.class);
                startActivity(i);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RECOGNIZE_USER:

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.
                                    EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    speech.clear();
                    strSpeech2Text = strSpeech2Text.replace("ingrese su", "");
                    strSpeech2Text = strSpeech2Text.replace("su nombre", "");
                    strSpeech2Text = strSpeech2Text.replace("nombre de", "");
                    strSpeech2Text = strSpeech2Text.replace("de usuario", "");
                    strSpeech2Text = strSpeech2Text.replace(" ", "");
                    Toast.makeText(getApplicationContext(), strSpeech2Text, Toast.LENGTH_SHORT).show();

                    mEmailView.setText(strSpeech2Text);
                }

                break;
            case RECOGNIZE_PASS:

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> speech = data
                            .getStringArrayListExtra(RecognizerIntent.
                                    EXTRA_RESULTS);
                    String strSpeech2Text = speech.get(0);
                    speech.clear();
                    strSpeech2Text = strSpeech2Text.replace("ingrese su", "");
                    strSpeech2Text = strSpeech2Text.replace("su contraseña", "");
                    strSpeech2Text = strSpeech2Text.replace(" ", "");
                    mPasswordView.setText(strSpeech2Text);
                }

                break;
            default:

                break;
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }  else if (!consultaporcodigo(mEmailView.getText().toString())){
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);
            Intent accountsIntent = new Intent(getApplicationContext(), MainActivity.class);
            accountsIntent.putExtra("user", mEmailView.getText().toString().trim());
            mEmailView.setText("");
            showProgress(false);
            startActivity(accountsIntent);

            //consultar(email,password);
            //UserLogin();


        }
    }

    private boolean comprobandoDatos(AutoCompleteTextView user, EditText pass) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int valor=0;
        Cursor fila = bd.rawQuery(
                "select dni from empleado where username='"+user+"' & pass ='"+pass+"'", null);
        if (fila.moveToFirst()) {
            valor = Integer.valueOf(fila.getString(0));
            bd.close();
            Toast.makeText(this, valor+" ",
                    Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Usuario no registrado",
                    Toast.LENGTH_SHORT).show();
            return false;}
        if(valor!=0)
            return true;
        else
            return false;
    }

    private boolean isIDValid(String email) {   ///AÑADIIIIIIIIIIIIIIIIIIIIIIIIIIIIR
        //TODO: Replace this with your own logic

        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,

                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,


                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final Context mContext;


        UserLoginTask(String email, String password, Context context) {
            mEmail = email;
            mPassword = password;
            mContext= context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,
                    "administracion", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            String query = "select count(*) from empleado where username='"+this.mEmail+"' and pass='"+mPassword+"'";
            Cursor fila = bd.rawQuery(query, null);
            if (fila.moveToFirst()) {
                CODE = Integer.parseInt(fila.getString(0));

            } else
                Toast.makeText(mContext, "No existe un artículo con dicho código",Toast.LENGTH_SHORT).show();
            bd.close();
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Toast.makeText(mContext, CODE+"",Toast.LENGTH_SHORT).show();
            if (success) {
                if (CODE==1){
                    finish();
                    Intent myIntent = new Intent(LoginActivity.this,MainActivity.class);

                    LoginActivity.this.startActivity(myIntent);
                    CODE=0;
                } else {


                }
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    public boolean insertarUsuarios(int dni, String nombre, String apellido, String correo, String username, String pass) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dni", dni);
        contentValues.put("nombre", nombre);
        contentValues.put("apellido", apellido);
        contentValues.put("correo", correo);
        contentValues.put("username", username);
        contentValues.put("pass", pass);
        bd.insert("empleado", null, contentValues);
        bd.close();
        return true;
    }

    public void insertSomeUsers() {

        insertarUsuarios(19231000,"Juan", "Nacarino","j.n@gmail.com","JuanNa","123456");
        insertarUsuarios(71491612,"Pedro","Alvarez","pedroangel.alvarez@gmail.com","Pedro2408","flechaverde");

    }
    public boolean consultaporcodigo(String dni) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String valor="";
        Cursor fila = bd.rawQuery(
                "select pass from empleado where username='" + dni+"'", null);
        if (fila.moveToFirst()) {
            valor = fila.getString(0);
            bd.close();

        } else{
            Toast.makeText(this, "Usuario no registrado",
                    Toast.LENGTH_SHORT).show();
            return false;}
        if(valor.equals(mPasswordView.getText().toString()))
            return true;
        else
            return false;

    }
}