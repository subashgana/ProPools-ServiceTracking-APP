package com.servicetracker.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.servicetracker.R;
import com.servicetracker.adapter.CustomSpinnerAdapter;
import com.servicetracker.constants.CommonUtils;
import com.servicetracker.constants.Constants;
import com.servicetracker.constants.TakePictureUtils;
import com.servicetracker.constants.TouchImageView;
import com.servicetracker.database.DatabaseHandler;
import com.servicetracker.model.UserListModel;
import com.servicetracker.service.BackgroundService;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import eu.janmuller.android.simplecropimage.CropImage;

/**
 * Created by pushpender.singh on 1/12/15.
 */
public class CustomerInformationActivity extends Activity implements OnClickListener {
    public static final int TAKE_PICTURE = 101;
    public static final int PICK_GALLERY = 201;
    public static final int CROP_FROM_CAMERA = 301;
    private static final int CAMERA_REQUEST = 1888;
    public static String mAction;
    public static Tag mTag;
    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };
    private TextView tvHardness, tvTotalChlorine,
            tvFreeChlorine, tvPh, tvAlkalinity, tvConditioner1,
            tvCHLPowder, tvCHLTabltvs, tvSodiumCHL, tvPHplus, tvConditioner2,
            tvSuperBLue, tvMentalGone, tvMentalOut, tvAlgaecide, tvAlkalinityCount, tvAcid, tvPhMinus;
    private Spinner spHardness, spTotalChlorine,
            spFreeChlorine, spPh, spAlkalinity, spConditioner1,
            spCHLPowder, spCHLTablets, spSodiumCHL, spPHplus, spConditioner2,
            spSuperBLue, spMentalGone, spMentalOut, spAlgaecide, spAlkalinityCount, spAcid, spPhMinus;

    private EditText etName, etNFCid, etAddress, etMisc;
    private ImageView ivPhoto1, ivPhoto2, ivPhot3, ivPhoto4;
    private TextView tvStartService, tvSubmit;
    private String imageEncoded;
    private Bitmap bm;
    private BitmapFactory.Options options = new BitmapFactory.Options();
    private String imageName = "", path = "";
    private byte[] data;
    private Intent intent;
    private Context context;
    private boolean photo1, photo2, photo3, photo4;
    private DatabaseHandler handler;
    private String user_id;
    private UserListModel userDetail;
    private ArrayList<String> arlHardness;
    private ArrayList<String> arlTotalChlorine;
    private ArrayList<String> arlFreeChlorine;
    private ArrayList<String> arlPh;
    private ArrayList<String> arlAlkalinity;
    private ArrayList<String> arlConditioner1;
    private ArrayList<String> arlChlPowder;
    private ArrayList<String> arlChlTablets;
    private ArrayList<String> arlSuperBlue;
    private ArrayList<String> arlAcid;
    private ArrayList<String> arlAlkalinityCount, arlPhPlus, arlPhMinus, arlSodiumChl, arlMentalGone, arlMentalOut, arlAlgaecide;
    private String TAG = CustomerInformationActivity.class.getSimpleName();
    private CustomSpinnerAdapter adapter;
    private String image1, image2, image3, image4, endtime = "", starttime = "";
    private boolean beforeScan = true, afterScan = false;
    private Uri mCapturedImageURI;
    private ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_new_info);
        getIds();
        setListners();


        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDetail = new UserListModel();
        user_id = getIntent().getExtras().getString("user_id");
        context = CustomerInformationActivity.this;
        handler = new DatabaseHandler(CustomerInformationActivity.this);
        arlHardness = new ArrayList<>();
        arlTotalChlorine = new ArrayList<>();
        arlFreeChlorine = new ArrayList<>();
        arlPh = new ArrayList<>();
        arlAlkalinity = new ArrayList<>();
        arlChlPowder = new ArrayList<>();
        arlConditioner1 = new ArrayList<>();
        arlChlTablets = new ArrayList<>();
        arlSuperBlue = new ArrayList<>();
        arlAcid = new ArrayList<>();
        arlAlkalinityCount = new ArrayList<>();
        arlPhPlus = new ArrayList<>();
        arlPhMinus = new ArrayList<>();
        arlSodiumChl = new ArrayList<>();
        arlMentalGone = new ArrayList<>();
        arlMentalOut = new ArrayList<>();
        arlAlgaecide = new ArrayList<>();

        // Harness
        for (int i = 0; i < getResources().getStringArray(R.array.arl_hardness).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_hardness)[i]);
            arlHardness.add(getResources().getStringArray(R.array.arl_hardness)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlHardness);
        spHardness.setAdapter(adapter);

        // Total Chlorine
        for (int i = 0; i < getResources().getStringArray(R.array.arl_total_chlorine).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_total_chlorine)[i]);
            arlTotalChlorine.add(getResources().getStringArray(R.array.arl_total_chlorine)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlTotalChlorine);
        spTotalChlorine.setAdapter(adapter);

        // Free Chlorine
        for (int i = 0; i < getResources().getStringArray(R.array.arl_free_chlorine).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_free_chlorine)[i]);
            arlFreeChlorine.add(getResources().getStringArray(R.array.arl_free_chlorine)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlFreeChlorine);
        spFreeChlorine.setAdapter(adapter);

        // PH
        for (int i = 0; i < getResources().getStringArray(R.array.arl_ph).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_ph)[i]);
            arlPh.add(getResources().getStringArray(R.array.arl_ph)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlPh);
        spPh.setAdapter(adapter);

        // Alkalinity
        for (int i = 0; i < getResources().getStringArray(R.array.arl_alkalinity).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_alkalinity)[i]);
            arlAlkalinity.add(getResources().getStringArray(R.array.arl_alkalinity)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlAlkalinity);
        spAlkalinity.setAdapter(adapter);

        // Conditioner
        for (int i = 0; i < getResources().getStringArray(R.array.arl_conditioner1).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_conditioner1)[i]);
            arlConditioner1.add(getResources().getStringArray(R.array.arl_conditioner1)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlConditioner1);
        spConditioner1.setAdapter(adapter);

        // CHL Tablets
        for (int i = 0; i < getResources().getStringArray(R.array.arl_chl_tablets).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_chl_tablets)[i]);
            arlChlTablets.add(getResources().getStringArray(R.array.arl_chl_tablets)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlChlTablets);
        spCHLTablets.setAdapter(adapter);

        // Acid
        for (int i = 0; i < getResources().getStringArray(R.array.arl_acid).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_acid)[i]);
            arlAcid.add(getResources().getStringArray(R.array.arl_acid)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlAcid);
        spAcid.setAdapter(adapter);

        // Alkalinity Count
        for (int i = 0; i < getResources().getStringArray(R.array.arl_alkalinity_count).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_alkalinity_count)[i]);
            arlAlkalinityCount.add(getResources().getStringArray(R.array.arl_alkalinity_count)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlAlkalinityCount);
        spAlkalinityCount.setAdapter(adapter);

        // PH Plus
        for (int i = 0; i < getResources().getStringArray(R.array.arl_ph_plus).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_ph_plus)[i]);
            arlPhPlus.add(getResources().getStringArray(R.array.arl_ph_plus)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlPhPlus);
        spPHplus.setAdapter(adapter);

        // PH Minus
        for (int i = 0; i < getResources().getStringArray(R.array.arl_ph_minus).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_ph_minus)[i]);
            arlPhMinus.add(getResources().getStringArray(R.array.arl_ph_minus)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlPhMinus);
        spPhMinus.setAdapter(adapter);

        // Sodium CHL
        for (int i = 0; i < getResources().getStringArray(R.array.arl_sodium_chl).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_sodium_chl)[i]);
            arlSodiumChl.add(getResources().getStringArray(R.array.arl_sodium_chl)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlSodiumChl);
        spSodiumCHL.setAdapter(adapter);

        // CHL Powder
        for (int i = 0; i < getResources().getStringArray(R.array.arl_chl_powder).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_chl_powder)[i]);
            arlChlPowder.add(getResources().getStringArray(R.array.arl_chl_powder)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlChlPowder);
        spCHLPowder.setAdapter(adapter);

        // TODO
        spConditioner2.setAdapter(adapter);

        // Super Blue
        for (int i = 0; i < getResources().getStringArray(R.array.arl_super_blue).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_super_blue)[i]);
            arlSuperBlue.add(getResources().getStringArray(R.array.arl_super_blue)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlSuperBlue);
        spSuperBLue.setAdapter(adapter);

        // Mental Gone
        for (int i = 0; i < getResources().getStringArray(R.array.arl_metal_gone).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_metal_gone)[i]);
            arlMentalGone.add(getResources().getStringArray(R.array.arl_metal_gone)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlMentalGone);
        spMentalGone.setAdapter(adapter);

        // Mental Out
        for (int i = 0; i < getResources().getStringArray(R.array.arl_metal_out).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_metal_out)[i]);
            arlMentalOut.add(getResources().getStringArray(R.array.arl_metal_out)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlMentalOut);
        spMentalOut.setAdapter(adapter);

        // Algaecide
        for (int i = 0; i < getResources().getStringArray(R.array.arl_algaecide).length; i++) {
            CommonUtils.printLog(TAG, "value is" + getResources().getStringArray(R.array.arl_algaecide)[i]);
            arlAlgaecide.add(getResources().getStringArray(R.array.arl_algaecide)[i]);
        }
        adapter = new CustomSpinnerAdapter(CustomerInformationActivity.this, arlAlgaecide);
        spAlgaecide.setAdapter(adapter);


        // TODO Item Selected Listener
        spHardness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvHardness.setText(arlHardness.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spTotalChlorine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvTotalChlorine.setText(arlTotalChlorine.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spFreeChlorine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvFreeChlorine.setText(arlFreeChlorine.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spPh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvPh.setText(arlPh.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAlkalinity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvAlkalinity.setText(arlAlkalinity.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spConditioner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvConditioner1.setText(arlConditioner1.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCHLPowder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvCHLPowder.setText(arlChlPowder.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAlkalinityCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvAlkalinityCount.setText(arlAlkalinityCount.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPHplus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvPHplus.setText(arlPhPlus.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPhMinus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvPhMinus.setText(arlPhMinus.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spConditioner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvConditioner2.setText(arlChlPowder.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spSodiumCHL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CommonUtils.printLog(TAG, "inside sodium" + arlSodiumChl.get(position));
                if (position > 0) {
                    tvSodiumCHL.setText(arlSodiumChl.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCHLTablets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvCHLTabltvs.setText(arlChlTablets.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMentalGone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvMentalGone.setText(arlMentalGone.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSuperBLue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvSuperBLue.setText(arlSuperBlue.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMentalOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvMentalOut.setText(arlMentalOut.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAlgaecide.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvAlgaecide.setText(arlAlgaecide.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spAcid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvAcid.setText(arlAcid.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("image1", image1);
        outState.putString("image2", image2);
        outState.putString("image3", image3);
        outState.putString("image4", image4);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("image1")) {
            image1 = savedInstanceState.getString("image1");
            image2 = savedInstanceState.getString("image2");
            image3 = savedInstanceState.getString("image3");
            image4 = savedInstanceState.getString("image4");
        }
    }

    private void setListners() {
        tvSubmit.setOnClickListener(this);
        ivPhoto1.setOnClickListener(this);
        ivPhoto2.setOnClickListener(this);
        ivPhot3.setOnClickListener(this);
        ivPhoto4.setOnClickListener(this);
        tvHardness.setOnClickListener(this);
        tvPhMinus.setOnClickListener(this);
        tvTotalChlorine.setOnClickListener(this);
        tvFreeChlorine.setOnClickListener(this);
        tvPh.setOnClickListener(this);
        tvAlkalinity.setOnClickListener(this);
        tvConditioner1.setOnClickListener(this);
        tvCHLPowder.setOnClickListener(this);
        tvCHLTabltvs.setOnClickListener(this);
        tvSodiumCHL.setOnClickListener(this);
        tvConditioner2.setOnClickListener(this);
        tvMentalGone.setOnClickListener(this);
        tvMentalOut.setOnClickListener(this);
        tvAlgaecide.setOnClickListener(this);
        tvAlkalinity.setOnClickListener(this);
        tvAcid.setOnClickListener(this);
        tvAlkalinityCount.setOnClickListener(this);
        tvSuperBLue.setOnClickListener(this);
        tvPHplus.setOnClickListener(this);
    }

    private void getIds() {
        tvHardness = (TextView) findViewById(R.id.tvHardness);
        tvPhMinus = (TextView) findViewById(R.id.tvPHminus);
        tvTotalChlorine = (TextView) findViewById(R.id.tvTotalChlorine);
        tvFreeChlorine = (TextView) findViewById(R.id.tvFreeChlorine);
        tvPh = (TextView) findViewById(R.id.tvPH);
        tvAlkalinity = (TextView) findViewById(R.id.tvAlkalinity);
        tvConditioner1 = (TextView) findViewById(R.id.tvConditioner1);
        tvCHLPowder = (TextView) findViewById(R.id.tvCHLPowder);
        tvCHLTabltvs = (TextView) findViewById(R.id.tvCHLTablets);
        tvSodiumCHL = (TextView) findViewById(R.id.tvSodiumCHL);
        tvPHplus = (TextView) findViewById(R.id.tvPHPLus);
        tvConditioner2 = (TextView) findViewById(R.id.tvConditioner2);
        tvSuperBLue = (TextView) findViewById(R.id.tvSuperBlue);
        tvMentalGone = (TextView) findViewById(R.id.tvMetalGone);
        tvMentalOut = (TextView) findViewById(R.id.tvMetalOut);
        tvAlgaecide = (TextView) findViewById(R.id.tvAlgaecide);
        tvAcid = (TextView) findViewById(R.id.tvAcid);
        tvAlkalinityCount = (TextView) findViewById(R.id.tvAlkalinityCount);
        tvSuperBLue = (TextView) findViewById(R.id.tvSuperBlue);

        etName = (EditText) findViewById(R.id.etName);
        etNFCid = (EditText) findViewById(R.id.etNFCid);
        etAddress = (EditText) findViewById(R.id.etAddress);
        spHardness = (Spinner) findViewById(R.id.spHardness);
        spPhMinus = (Spinner) findViewById(R.id.spPHminus);
        spTotalChlorine = (Spinner) findViewById(R.id.spTotalChlorine);
        spFreeChlorine = (Spinner) findViewById(R.id.spFreeChlorine);
        spPh = (Spinner) findViewById(R.id.spPH);
        spAlkalinity = (Spinner) findViewById(R.id.spAlkalinity);
        spConditioner1 = (Spinner) findViewById(R.id.spConditioner1);
        spCHLPowder = (Spinner) findViewById(R.id.spCHLPowder);
        spCHLTablets = (Spinner) findViewById(R.id.spCHLTablets);
        spSodiumCHL = (Spinner) findViewById(R.id.spSodiumCHL);
        spPHplus = (Spinner) findViewById(R.id.spPHPLus);
        spConditioner2 = (Spinner) findViewById(R.id.spConditioner2);
        spSuperBLue = (Spinner) findViewById(R.id.spSuperBlue);
        spMentalGone = (Spinner) findViewById(R.id.spMetalGone);
        spMentalOut = (Spinner) findViewById(R.id.spMetalOut);
        spAlgaecide = (Spinner) findViewById(R.id.spAlgaecide);
        spAlkalinityCount = (Spinner) findViewById(R.id.spAlkalinityCount);
        spAcid = (Spinner) findViewById(R.id.spAcid);
        etMisc = (EditText) findViewById(R.id.etMisc);
        ivPhoto1 = (ImageView) findViewById(R.id.ivPhoto1);
        ivPhoto2 = (ImageView) findViewById(R.id.ivPhoto2);
        ivPhot3 = (ImageView) findViewById(R.id.ivPhoto3);
        ivPhoto4 = (ImageView) findViewById(R.id.ivPhoto4);
        tvStartService = (TextView) findViewById(R.id.tvStartService);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvSubmit:
                if (validation()) {
                    if (CommonUtils.isOnline(CustomerInformationActivity.this)) {
                        userDetail.setUserid(user_id);
                        userDetail.setField_nfc_id(etNFCid.getText().toString().trim());
                        userDetail.setName(etName.getText().toString().trim());
                        userDetail.setAddress(etAddress.getText().toString().trim());
                        Log.e("hardness", "hardemsess" + tvHardness.getText().toString().trim());
                        userDetail.setHardness(tvHardness.getText().toString().trim());
                        userDetail.setTotalchlorine(tvTotalChlorine.getText().toString().trim());
                        userDetail.setFreechlorine(tvFreeChlorine.getText().toString().trim());
                        userDetail.setPh(tvPh.getText().toString().trim());
                        userDetail.setConditioner(tvConditioner1.getText().toString().trim());
                        userDetail.setChlpowder(tvCHLPowder.getText().toString().trim());
                        userDetail.setChltablets(tvCHLTabltvs.getText().toString().trim());
                        userDetail.setSodiumchl(tvSodiumCHL.getText().toString().trim());
                        userDetail.setPhplus(tvPHplus.getText().toString().trim());
                        userDetail.setPhminus(tvPhMinus.getText().toString().trim());
                        userDetail.setChemicalconditioner(tvConditioner2.getText().toString().trim());
                        userDetail.setSuperblue(tvSuperBLue.getText().toString().trim());
                        userDetail.setMetalgone(tvMentalGone.getText().toString().trim());
                        userDetail.setField_id_no(etAddress.getText().toString().trim());
                        userDetail.setMetalout(tvMentalOut.getText().toString().trim());
                        userDetail.setAlgaecide(tvAlgaecide.getText().toString().trim());
                        userDetail.setAlkaninitycont(tvAlkalinityCount.getText().toString().trim());
                        userDetail.setAlkalinity(tvAlkalinity.getText().toString().trim());
                        userDetail.setAcid(tvAcid.getText().toString().trim());
                        userDetail.setMiscnotes(etMisc.getText().toString().trim());
                        userDetail.setImage1(image1);
                        userDetail.setImage2(image2);
                        userDetail.setImage3(image3);
                        userDetail.setImage4(image4);
                        userDetail.setEndtime(endtime);
                        userDetail.setTimestamp(starttime);
                        handler.updateCustomerInformation(userDetail, "0", CommonUtils.getStringPreferences(CustomerInformationActivity.this, Constants.USERID));
                        intent = new Intent(CustomerInformationActivity.this, BackgroundService.class);
                        startService(intent);
                        EventBus.getDefault().post("2");
                        finish();
                    } else {
                        userDetail.setUserid(user_id);
                        userDetail.setField_nfc_id(etNFCid.getText().toString().trim());
                        userDetail.setName(etName.getText().toString().trim());
                        userDetail.setAddress(etAddress.getText().toString().trim());
                        Log.e("hardness", "hardemsess" + tvHardness.getText().toString().trim());
                        userDetail.setHardness(tvHardness.getText().toString().trim());
                        userDetail.setTotalchlorine(tvTotalChlorine.getText().toString().trim());
                        userDetail.setFreechlorine(tvFreeChlorine.getText().toString().trim());
                        userDetail.setPh(tvPh.getText().toString().trim());
                        userDetail.setConditioner(tvConditioner1.getText().toString().trim());
                        userDetail.setChlpowder(tvCHLPowder.getText().toString().trim());
                        userDetail.setChltablets(tvCHLTabltvs.getText().toString().trim());
                        userDetail.setSodiumchl(tvSodiumCHL.getText().toString().trim());
                        userDetail.setPhplus(tvPHplus.getText().toString().trim());
                        userDetail.setPhminus(tvPhMinus.getText().toString().trim());
                        userDetail.setChemicalconditioner(tvConditioner2.getText().toString().trim());
                        userDetail.setSuperblue(tvSuperBLue.getText().toString().trim());
                        userDetail.setMetalgone(tvMentalGone.getText().toString().trim());
                        userDetail.setMetalout(tvMentalOut.getText().toString().trim());
                        userDetail.setAlgaecide(tvAlgaecide.getText().toString().trim());
                        userDetail.setAlkaninitycont(tvAlkalinityCount.getText().toString().trim());
                        userDetail.setAlkalinity(tvAlkalinity.getText().toString().trim());
                        userDetail.setField_id_no(etAddress.getText().toString().trim());
                        userDetail.setAcid(tvAcid.getText().toString().trim());
                        userDetail.setMiscnotes(etMisc.getText().toString().trim());
                        userDetail.setImage1(image1);
                        userDetail.setImage2(image2);
                        userDetail.setImage3(image3);
                        userDetail.setImage4(image4);
                        userDetail.setEndtime(endtime);
                        Log.e("image1", "image1" + image1);
                        Log.e("image2", "image2" + image2);
                        Log.e("image3", "image3" + image3);
                        Log.e("image4", "image4" + image4);
                        userDetail.setTimestamp(starttime);
                        handler.updateCustomerInformation(userDetail, "0", CommonUtils.getStringPreferences(CustomerInformationActivity.this, Constants.USERID));
                        EventBus.getDefault().post("1");
                        finish();
                    }
                }
                break;
            case R.id.ivPhoto1:
                photo1 = true;
                photo2 = false;
                photo3 = false;
                photo4 = false;

                imageName = "picture_" + System.currentTimeMillis();
                takePicture(CustomerInformationActivity.this, imageName);
                break;
            case R.id.ivPhoto2:
                photo1 = false;
                photo2 = true;
                photo3 = false;
                photo4 = false;
                imageName = "picture_" + System.currentTimeMillis();
                takePicture(CustomerInformationActivity.this, imageName);

                break;
            case R.id.ivPhoto3:
                photo1 = false;
                photo2 = false;
                photo3 = true;
                photo4 = false;
                imageName = "picture_" + System.currentTimeMillis();
                takePicture(CustomerInformationActivity.this, imageName);

                break;
            case R.id.ivPhoto4:
                photo1 = false;
                photo2 = false;
                photo3 = false;
                photo4 = true;
                imageName = "picture_" + System.currentTimeMillis();
                takePicture(CustomerInformationActivity.this, imageName);

                break;

            case R.id.tvHardness:
                spHardness.performClick();
                break;
            case R.id.tvPHminus:
                spPhMinus.performClick();
                break;
            case R.id.tvTotalChlorine:
                spTotalChlorine.performClick();
                break;
            case R.id.tvFreeChlorine:
                spFreeChlorine.performClick();
                break;
            case R.id.tvPH:
                spPh.performClick();
                break;
            case R.id.tvConditioner1:
                spConditioner1.performClick();
                break;
            case R.id.tvCHLPowder:
                spCHLPowder.performClick();
                break;
            case R.id.tvCHLTablets:
                spCHLTablets.performClick();
                break;
            case R.id.tvSodiumCHL:
                spSodiumCHL.performClick();
                break;
            case R.id.tvConditioner2:
                spConditioner2.performClick();
                break;
            case R.id.tvMetalGone:
                spMentalGone.performClick();
                break;
            case R.id.tvMetalOut:
                spMentalOut.performClick();

                break;
            case R.id.tvAlgaecide:
                spAlgaecide.performClick();
                break;
            case R.id.tvPHPLus:
                spPHplus.performClick();
                break;
            case R.id.tvSuperBlue:
                spSuperBLue.performClick();
                break;
            case R.id.tvAlkalinityCount:
                spAlkalinityCount.performClick();
                break;
            case R.id.tvAcid:
                spAcid.performClick();
                break;
            case R.id.tvAlkalinity:
                spAlkalinity.performClick();
                break;
        }
    }

    private boolean validation() {
        if (etNFCid.getText().toString().length() == 0) {
            CommonUtils.showAlertTitle(getString(R.string.app_name),
                    getString(R.string.please_touch_the_phone_with_nfc_tag), this);
            return false;
        } else if (image1 == null) {
            CommonUtils.showAlertTitle(getString(R.string.app_name),
                    getString(R.string.please_upload_atleast_one_photo), this);
            return false;
        } else if (endtime == null || endtime.equalsIgnoreCase("")) {
            CommonUtils.showAlertTitle(getString(R.string.app_name),
                    getString(R.string.please_touch_nfc_to_complet_transaction), this);
            return false;
        }

        return true;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bm = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), bm);
            path = BitMapToString(bm);
            Log.e("imagepath", "imagepath " + path);
            if (path != null && path.length() > 0) {
                Log.e("path", "path" + path);
                if (photo1) {
                    image1 = path;
                } else if (photo2) {
                    image2 = path;
                } else if (photo3) {
                    image3 = path;
                } else if (photo4) {
                    image4 = path;
                }
            }
            if (photo1) {
                ivPhoto1.setImageBitmap(bm);
            } else if (photo2) {
                ivPhoto2.setImageBitmap(bm);
            } else if (photo3) {
                ivPhot3.setImageBitmap(bm);
            } else if (photo4) {
                ivPhoto4.setImageBitmap(bm);
            }


        }

        if (requestCode == PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    InputStream inputStream = CustomerInformationActivity.this.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(getExternalFilesDir("temp"), imageName + ".jpg"));
                    TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage(CustomerInformationActivity.this, imageName + ".jpg");
                } catch (Exception e) {
                    Toast.makeText(CustomerInformationActivity.this, "Error in picture", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == TAKE_PICTURE) {
            if (resultCode == -1) {
                String path = new File(context.getExternalFilesDir("temp"), imageName + ".jpg").getPath();

                if (path != null && path.length() > 0) {
                    Log.e("path", "path" + path);
                    if (photo1) {
                        image1 = path;
                    } else if (photo2) {
                        image2 = path;
                    } else if (photo3) {
                        image3 = path;
                    } else if (photo4) {
                        image4 = path;
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bm = BitmapFactory.decodeFile(path, options);
                    if (photo1) {
                        ivPhoto1.setImageBitmap(bm);
                    } else if (photo2) {
                        ivPhoto2.setImageBitmap(bm);
                    } else if (photo3) {
                        ivPhot3.setImageBitmap(bm);
                    } else if (photo4) {
                        ivPhoto4.setImageBitmap(bm);
                    }
                }
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    /**
     * this method is used for take picture from camera
     */
    public void takePicture(Activity context, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri = null;
            mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, TAKE_PICTURE);
        } catch (ActivityNotFoundException ignored) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * this method is used for open crop image
     */
    public void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_X, 400);
        intent.putExtra(CropImage.OUTPUT_Y, 400);
        context.startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            // creating intent receiver for NFC events:
            IntentFilter filter = new IntentFilter();
            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
            // enabling foreground dispatch for getting intent from NFC event:
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            if (nfcAdapter != null) {
                if (nfcAdapter.isEnabled()) {
                    nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);

                } else {
                    CommonUtils.showToast(CustomerInformationActivity.this, getString(R.string.please_enable_your_nfc_settings));
                    finish();
                }
            } else {
                CommonUtils.showToast(CustomerInformationActivity.this, getString(R.string.sorry_device_doesnt_support_nfc));
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        try {
            if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                String id = "" + ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                // String id = "04:19:69:52:78:3f:81";
                if (id != null && id.length() > 0) {
                    Log.e("", "NFC Value>>>>> " + id.toUpperCase());
                    Toast.makeText(CustomerInformationActivity.this, getString(R.string.nfc_tag_detected), Toast.LENGTH_SHORT).show();
                    if (user_id != null) {
                        if (beforeScan) {
                            if (handler.checkVaildNFCId(id.toUpperCase().trim(), user_id)) {
                                starttime = String.valueOf(System.currentTimeMillis() / 1000);
                                afterScan = true;
                                beforeScan = false;
                                tvSubmit.setEnabled(true);
                                Log.e("name ", "name " + handler.getSingleContactDetails(user_id).get(0).getName());
                                etName.setText("" + handler.getSingleContactDetails(user_id).get(0).getName());
                                etNFCid.setText(handler.getSingleContactDetails(user_id).get(0).getField_nfc_id());
                                etAddress.setText(handler.getSingleContactDetails(user_id).get(0).getField_id_no());
                            } else {
                                Toast.makeText(CustomerInformationActivity.this, getString(R.string.nfc_tag_not_valid_for_custom), Toast.LENGTH_SHORT).show();
                            }
                        } else if (afterScan) {
                            if (handler.checkVaildNFCId(id.toUpperCase().trim(), user_id)) {
                                endtime = String.valueOf(System.currentTimeMillis() / 1000);
                            } else {
                                Toast.makeText(CustomerInformationActivity.this, getString(R.string.nfc_tag_not_valid_for_custom), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            if (nfcAdapter != null) {
                if (nfcAdapter.isEnabled()) {
                    nfcAdapter.disableForegroundDispatch(this);
                } else {
                    CommonUtils.showToast(CustomerInformationActivity.this, getString(R.string.please_enable_your_nfc_settings));
                    finish();
                }
            } else {
                CommonUtils.showToast(CustomerInformationActivity.this,  getString(R.string.sorry_device_doesnt_support_nfc));
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Log.e(TAG, "onBackPressed" + starttime + ":::");
        if (starttime != null && starttime.length()>0) {
            infoLossAlert();
        } else {
            finish();
        }
    }

    public void infoLossAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.inform_will_lost_do_you_want_to_exit))
                .setCancelable(false)
                .setTitle(R.string.app_name)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                infoLossExitAlert();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void infoLossExitAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.warning_infor_will_lost))
                .setCancelable(false)
                .setTitle(R.string.app_name)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // TODO what action need to perform. HOME screen naviagation
                                finish();

                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*when a formed is opened and nfc scanned for the start time and the back
    button is pressed i would like a warning to open stating " THE INFORMATION
    IN THIS FORM WILL BE LOST, DO YOU WISH TO EXIT - YES OR NO" If yes is
    selected i would like another box to open with the following " WARNING
    INFORMATION WILL BE LOST, ARE YOU SURE - YES OR NO" if yes is selected then
    the form would be exited.*/

}