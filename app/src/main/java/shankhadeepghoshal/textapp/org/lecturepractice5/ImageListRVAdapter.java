package shankhadeepghoshal.textapp.org.lecturepractice5;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageListRVAdapter extends RecyclerView.Adapter<ImageListRVAdapter.ImageListViewHolder> {

    private ArrayList<Uri> imageList;
    private ArrayList<String> filePaths;
    private static ClickListener clickListener;

    public ImageListRVAdapter(ArrayList<Uri> imageList) {
        this.imageList = imageList;
        this.filePaths = new ArrayList<>();
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_layout,parent,false));
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ImageListViewHolder holder, int position) {
        Uri tempUri = this.imageList.get(position);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(holder.getImageView().getContext().getContentResolver(), tempUri);
            holder.getImageView().setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String res;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = holder.getTextView()
                .getContext().getContentResolver()
                .query(tempUri,proj,null,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                int col_index = cursor.getColumnIndexOrThrow(proj[0]);
                res = cursor.getString(col_index);
                holder.getTextView().setText(res);
                this.filePaths.add(res);
            }
            cursor.close();
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.imageList.size();
    }

    public void addEntry(Uri uri){
        this.imageList.add(uri);
        notifyDataSetChanged();
    }

    public String getFilePath(int position){
        return this.filePaths.get(position);
    }

    public void addOnClickListener(final ClickListener clickListener){
        ImageListRVAdapter.clickListener = clickListener;
    }


    public static class ImageListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView textView;

        public ImageListViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.textView = itemView.findViewById(R.id.textViewImageCaption);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public void onClick(View view) {
            ImageListRVAdapter.clickListener.onItemClicked(getAdapterPosition(),view);
        }
    }

    public interface ClickListener{
        void onItemClicked(int position, View view);
    }
}
