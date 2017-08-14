package com.android.trikh.focusLock.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.trikh.focusLock.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.android.trikh.focusLock.PreferenceHelper.getFirstDay;

/**
 * Created by trikh on 04-08-2017.
 */

public class FreeVideosFragment extends Fragment {

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;
    final String[] VIDEO_ID = {"26U_seo0a1g", "VFeXCKrAD-U", "CPQ1budJRIQ", "xp2qjshr-r4", "HeGPn5zxegY", "bYMUb4uQZoo", "lG_rrVwSqEs"};
    @BindView(R.id.content_title)
    TextView title;
    @BindView(R.id.thumbnail)
    ImageView thumbnail;
    @BindView(R.id.play_button)
    ImageView playButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.motivational_video_frag_layout, container, false);

        ButterKnife.bind(this, rootView);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Rajdhani-Bold.ttf");
        title.setTypeface(typeface);

        int dayToday = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int videoNumber = dayToday - getFirstDay(getContext());
        if (videoNumber <= 6) {
            final String video_id = VIDEO_ID[videoNumber];

            Picasso.with(getContext()).load("http://img.youtube.com/vi/" + video_id + "/hqdefault.jpg").into(thumbnail);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                            getActivity(), getString(R.string.youTube_dev_key), video_id, 0, false, true);

                    if (intent != null) {
                        if (canResolveIntent(intent)) {
                            startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                        } else {
                            // Could not resolve the intent - must need to install or update the YouTube API service.
                            YouTubeInitializationResult.SERVICE_MISSING
                                    .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
                        }
                    }
                }
            });
            Toast.makeText(getContext(), "Daily content left for free Version: " + String.valueOf(7 - videoNumber), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Daily content left for free Version: 0", Toast.LENGTH_SHORT).show();
            playButton.setVisibility(View.GONE);
            thumbnail.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(getActivity(), 0).show();
            } else {
                String errorMessage =
                        String.format(getString(R.string.error_player), errorReason.toString());
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }
}
