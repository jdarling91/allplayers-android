package com.allplayers.android;

import com.allplayers.objects.PhotoData;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class PhotoDisplayActivity extends Activity implements OnTouchListener {
    float  downYValue;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photodisplay);

        PhotoData photo = Globals.currentPhoto;
        String photoUrl = photo.getPhotoFull();

        Bitmap image = Globals.getRemoteImage(photoUrl);

        ImageView imView = (ImageView)findViewById(R.id.fullPhotoDisplay);
        imView.setImageBitmap(image);

        imView.setOnTouchListener((OnTouchListener) this);
    }

    public boolean onTouch(View arg0, MotionEvent arg1) {
        switch (arg1.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            downYValue = arg1.getX();
            break;
        }

        case MotionEvent.ACTION_UP: {
            float currentX = arg1.getX();

            if (downYValue < currentX) {
                ViewFlipper slider = (ViewFlipper)findViewById(R.id.Slider);
                slider.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
                slider.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));

                //change image
                if (Globals.currentPhoto.previousPhoto() != null) {
                    Globals.currentPhoto = Globals.currentPhoto.previousPhoto();
                }

                PhotoData photo = Globals.currentPhoto;
                String photoUrl = photo.getPhotoFull();
                Bitmap image = Globals.getRemoteImage(photoUrl);

                ImageView imView = (ImageView)findViewById(R.id.fullPhotoDisplay);
                imView.setImageBitmap(image);
            }

            if (downYValue > currentX) {
                ViewFlipper slider = (ViewFlipper) findViewById(R.id.Slider);
                slider.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
                slider.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));

                //change image
                if (Globals.currentPhoto.nextPhoto() != null) {
                    Globals.currentPhoto = Globals.currentPhoto.nextPhoto();
                }

                PhotoData photo = Globals.currentPhoto;
                String photoUrl = photo.getPhotoFull();
                Bitmap image = Globals.getRemoteImage(photoUrl);

                ImageView imView = (ImageView)findViewById(R.id.fullPhotoDisplay);
                imView.setImageBitmap(image);
            }

            break;
        }
        }

        return true;
    }
}