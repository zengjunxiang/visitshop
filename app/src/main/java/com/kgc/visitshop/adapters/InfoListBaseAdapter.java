package com.kgc.visitshop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kgc.visitshop.R;
import com.kgc.visitshop.bean.InfoResultBody;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * HomeFragment资讯列表适配器
 */
public class InfoListBaseAdapter extends RecyclerView.Adapter<InfoListBaseAdapter.InfoViewHolder> {
    private Context mContext;
    private List<InfoResultBody> list;
    int number;

    public InfoListBaseAdapter(Context mContxt, List<InfoResultBody> list, int number) {
        this.mContext = mContxt;
        this.list = list;
        this.number = number;
    }

    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.fragment_home_info_item, null);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfoViewHolder holder, final int position) {
        InfoResultBody rd = list.get(position);
        holder.title.setText(rd.getTitle());
        holder.context.setText(rd.getSummary());
        if (!"".equals(rd.getImgurl().trim()) && rd.getImgurl() != null) {
            Picasso.with(mContext).load(rd.getImgurl()).into(holder.img);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到资讯详情
                Toast.makeText(mContext, "资讯详情界面敬请期待" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return number;
        } else {
            return 0;
        }
    }

    class InfoViewHolder extends RecyclerView.ViewHolder {
        ImageView img, arrow;
        TextView title, context;
        RelativeLayout root;

        public InfoViewHolder(View itemView) {
            super(itemView);
            arrow = (ImageView) itemView.findViewById(R.id.fragment_home_info_item_arrow);
            img = (ImageView) itemView.findViewById(R.id.fragment_home_info_item_img);
            context = (TextView) itemView.findViewById(R.id.fragment_home_info_item_context);
            title = (TextView) itemView.findViewById(R.id.fragment_home_info_item_title);
            root = (RelativeLayout) itemView.findViewById(R.id.item_root_home);
        }
    }

}
