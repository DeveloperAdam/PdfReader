package androidlab.com.pdfreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPageChangeListener,OnLoadCompleteListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SAMPLE_FILE = "merge.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    ZoomControls simpleZoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            customActionBar();
            pdfView= (PDFView)findViewById(R.id.pdfView);
            simpleZoomControls = (ZoomControls) findViewById(R.id.simpleZoomControl);
        displayFromAsset(SAMPLE_FILE);

        simpleZoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calculate current scale x and y value of ImageView
                float x = pdfView.getScaleX();
                float y = pdfView.getScaleY();
                // set increased value of scale x and y to perform zoom in functionality
                pdfView.setScaleX((float) (x + 1));
                pdfView.setScaleY((float) (y + 1));
                // display a toast to show Zoom In Message on Screen
                // hide the ZoomControls from the screen
            }
        });
        // perform setOnZoomOutClickListener event on ZoomControls
        simpleZoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calculate current scale x and y value of ImageView
                float x = pdfView.getScaleX();
                float y = pdfView.getScaleY();
                // set decreased value of scale x and y to perform zoom out functionality
                pdfView.setScaleX((float) (x - 1));
                pdfView.setScaleY((float) (y - 1));
                // display a toast to show Zoom Out Message on Screen
                // hide the ZoomControls from the screen
            }
        });

    }

    private void customActionBar() {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity)this).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        final TextView textView=(TextView)mCustomView.findViewById(R.id.tv);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private void displayFromAsset(String sampleFile) {
        pdfFileName = sampleFile;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .enableDoubletap(true)
                .swipeHorizontal(true)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
        pdfView.setMidZoom(50);

    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");



    }

    private void setMidZoom(float value) {
        pdfView.setMidZoom(value);
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}
