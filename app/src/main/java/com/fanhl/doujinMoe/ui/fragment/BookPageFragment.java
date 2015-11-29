package com.fanhl.doujinMoe.ui.fragment;


import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.PageApi;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.Page;
import com.fanhl.doujinMoe.ui.GalleryActivity;
import com.fanhl.photoview.PhotoViewAttacherEx;
import com.fanhl.util.GsonUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookPageFragment extends Fragment {
    public static final String TAG = BookPageFragment.class.getSimpleName();

    private static final String ARG_BOOK_DATA = "ARG_BOOK_DATA";
    private static final String ARG_POSITION  = "ARG_POSITION";

    @Bind(R.id.backgroundView)
    FrameLayout       mBackgroundView;
    @Bind(R.id.textView)
    AppCompatTextView mTextView;
    @Bind(R.id.imageView)
    PhotoView         mImageView;

    private Book book;
    private int  position;

    private PhotoViewAttacherEx mAttacher;

    /**
     * @param book
     * @param position
     * @return
     */
    public static BookPageFragment newInstance(Book book, int position) {
        BookPageFragment fragment = new BookPageFragment();
        Bundle           args     = new Bundle();
        args.putString(ARG_BOOK_DATA, GsonUtil.json(book));
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        book = GsonUtil.obj(bundle.getString(ARG_BOOK_DATA), Book.class);
        position = bundle.getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_page, container, false);
        ButterKnife.bind(this, view);

        mBackgroundView.setOnClickListener(view1 -> ((GalleryActivity) getActivity()).toggle());
        mTextView.setText(String.valueOf(position + 1));
        mAttacher = new PhotoViewAttacherEx(mImageView);
        mAttacher.setOnViewTapListener((view1, v, v1) -> ((GalleryActivity) getActivity()).toggle());

        refreshForOrientation(getResources().getConfiguration());

        if (book.isDownloaded() || PageApi.isPageDownloaded(getActivity(), book, position)) {
            Picasso.with(getActivity())
                    .load(PageApi.getPageFile(getActivity(), book, position))
                    .into(mImageView, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            mAttacher.update();
                        }
                    });
        } else {
            Page page = book.pages.get(position);
            Picasso.with(getActivity())
                    .load(page.href)
                    // FIXME: 15/11/10 Detail页面取得的preview
                    .into(mImageView, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            mAttacher.update();
                        }
                    });
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "横竖屏切换");
        refreshForOrientation(newConfig);
    }

    private void refreshForOrientation(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mAttacher.setScaleType(PhotoViewAttacherEx.ScaleTypeEx.TOP_CROP);
            // FIXME: 15/11/29 Top_Crop Bottom_Crop 处理
//            mAttacher.getDisplayMatrix().setTranslate(0, -mAttacher.getDisplayRect().height() / 2);

        }
    }
}
