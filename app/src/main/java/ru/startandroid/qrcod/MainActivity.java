package ru.startandroid.qrcod;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;


public class MainActivity extends ActionBarActivity implements QRCodeReaderView.OnQRCodeReadListener {


    private QRCodeReaderView mydecoderview;
    private boolean isSharing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQRCodeRead(final String s, PointF[] pointFs) {

        mydecoderview.getCameraManager().stopPreview();
        mydecoderview.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s)
                .setCancelable(false)
                .setTitle("QR Code!")
                .setPositiveButton("Сканировать еще...", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mydecoderview.getCameraManager().startPreview();
                        mydecoderview.setVisibility(View.VISIBLE);
                    }

                })
                .setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("Поделиться", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        isSharing = true;
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "QR code");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
                        startActivity(Intent.createChooser(sharingIntent, "Выберите приложение"));
                    }
                })
                ;
        AlertDialog alert = builder.create();
        alert.show();



    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mydecoderview.getVisibility() == View.VISIBLE) {
            mydecoderview.getCameraManager().stopPreview();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isSharing) {
            isSharing = false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Продолжить?")
                    .setCancelable(false)
                    .setTitle("QR Code!")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mydecoderview.getCameraManager().startPreview();
                            mydecoderview.setVisibility(View.VISIBLE);
                        }

                    })
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
        if (mydecoderview.getVisibility() == View.VISIBLE) {
            mydecoderview.getCameraManager().startPreview();
        }
    }
}
