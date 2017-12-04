package vn.edu.hust.set.tung.musicplayer.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import vn.edu.hust.set.tung.musicplayer.R;
import vn.edu.hust.set.tung.musicplayer.model.obj.Album;
import vn.edu.hust.set.tung.musicplayer.model.obj.Artist;
import vn.edu.hust.set.tung.musicplayer.model.obj.Song;

/**
 * Created by tungt on 12/04/17.
 */

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ObjViewHolder> {

    ArrayList<Object> listObj;

    public ObjectAdapter(ArrayList<Object> list) {
        listObj = list;
    }

    @Override
    public ObjViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_obj, parent, false);
        return new ObjViewHolder(view);
    }

    public ArrayList<Object> getListObj() {
        return listObj;
    }

    public void setListObj(ArrayList<Object> listObj) {
        this.listObj = listObj;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ObjViewHolder holder, int position) {
        Object object = listObj.get(position);
        if (object instanceof Song) {
            Song s = (Song) object;
            holder.tvNameObj.setText(s.getName());
            holder.ivIconObj.setImageResource(R.drawable.ic_song_accent);
        } else if (object instanceof Artist) {
            Artist a = (Artist) object;
            holder.tvNameObj.setText(a.getName());
            holder.ivIconObj.setImageResource(R.drawable.ic_artist_accent);
        } else if (object instanceof Album) {
            Album a = (Album) object;
            holder.tvNameObj.setText(a.getName());
            holder.ivIconObj.setImageResource(R.drawable.ic_album_accent);
        }
    }

    @Override
    public int getItemCount() {
        return listObj == null ? 0 : listObj.size();
    }

    public class ObjViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIconObj;
        TextView tvNameObj;

        public ObjViewHolder(View itemView) {
            super(itemView);
            ivIconObj = itemView.findViewById(R.id.ivIconObj);
            tvNameObj = itemView.findViewById(R.id.tvNameObj);
        }
    }
}
