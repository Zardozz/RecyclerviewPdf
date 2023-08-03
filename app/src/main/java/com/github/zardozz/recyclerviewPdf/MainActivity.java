package com.github.zardozz.recyclerviewPdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<String> myDataset = new ArrayList<>();

    private MyAdapter mAdapter;

    private final int A4Short = 594; // Postscript points
    private final int A4Long = 841; // Postscript points

    private File cacheFile;
    private String fileNameBase = "";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        for (int i = 1; i <= 191; i++) {
            myDataset.add(String.valueOf(i));
        }

        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

    }

    public void toPDF(View view) {

        int itemCount = mAdapter.getItemCount();
        if (itemCount > 0 ) {
            PdfDocument document = new PdfDocument();
            int pageNumber = 1;
            PdfDocument.PageInfo.Builder pageBuilder = new PdfDocument.PageInfo.Builder(A4Short, A4Long, pageNumber);
            PdfDocument.PageInfo pageInfo = pageBuilder.create();
            PdfDocument.Page page = document.startPage(pageInfo);

            int combinedHeight = 0;

            LinearLayout parent = new LinearLayout(getApplicationContext());
            parent.setOrientation(LinearLayout.VERTICAL);
            for (int index = 0; index < itemCount; index++) {
                // Get the required view
                MyAdapter.MyViewHolder vh = mAdapter.createViewHolder(parent, mAdapter.getItemViewType(index));
                mAdapter.bindViewHolder(vh, index);
                View item = vh.itemView;
                // Measure View to A4 width and as tall as it needs
                item.measure(View.MeasureSpec.makeMeasureSpec(A4Short, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                // Check combined height does not go off the page
                if ( combinedHeight + item.getMeasuredHeight() > A4Long) {
                    // End page and start next
                    // Layout the LinearLayout
                    parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    parent.measure(View.MeasureSpec.makeMeasureSpec(A4Short, View.MeasureSpec.AT_MOST),
                            View.MeasureSpec.makeMeasureSpec(A4Long, View.MeasureSpec.AT_MOST));
                    parent.layout(0, 0, parent.getMeasuredWidth(), parent.getMeasuredHeight());
                    Canvas pageCanvas = page.getCanvas();
                    // Draw current page
                    parent.draw(pageCanvas);
                    // End page and create next
                    document.finishPage(page);
                    // create new page
                    pageNumber += 1;
                    pageBuilder = new PdfDocument.PageInfo.Builder(A4Short, A4Long, pageNumber);
                    pageInfo = pageBuilder.create();
                    page = document.startPage(pageInfo);
                    // Clean up parent to re-use
                    parent.removeAllViews();
                    // Reset combinedHeight
                    combinedHeight = 0;

                }

                combinedHeight += item.getMeasuredHeight();
                parent.addView(item);
            }
            // Finish last page
            // Layout the LinearLayout
            parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.measure(View.MeasureSpec.makeMeasureSpec(A4Short, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(A4Long, View.MeasureSpec.AT_MOST));
            parent.layout(0, 0, parent.getMeasuredWidth(), parent.getMeasuredHeight());
            Canvas pageCanvas = page.getCanvas();
            // Draw current page
            parent.draw(pageCanvas);
            // End page
            document.finishPage(page);

            // Write document to cachefile
            // Create a dated filename
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss", Locale.ROOT);
            fileNameBase = "RecyclerViewPdf_" + df.format(Calendar.getInstance().getTime());
            cacheFile = new File(getCacheDir(), fileNameBase + ".pdf");

            // Create the pdf file in cache directory
            try {
                if (!cacheFile.exists()) {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
                    document.writeTo(bos);
                    bos.flush();
                    bos.close();
                } else {
                    throw new RuntimeException("Cache file exists when should not");
                }
            } catch (Exception e) {
                // Handle Error
            }

            // Clean up
            document.close();

            // Open PDF
            // Get the URI of the cache file from the FileProvider
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", cacheFile);
            if (uri != null) {
                // Create an intent to open the PDF in a third party app
                Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
                pdfViewIntent.setDataAndType(uri,"application/pdf");
                pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(pdfViewIntent, "Choose PDF viewer"));
            }
        }


    }
}