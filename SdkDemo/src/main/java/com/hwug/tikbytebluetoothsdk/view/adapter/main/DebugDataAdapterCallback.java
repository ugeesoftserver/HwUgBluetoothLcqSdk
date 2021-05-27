package com.hwug.tikbytebluetoothsdk.view.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hwug.tikbytebluetoothsdk.R;
import com.hwug.tikbytebluetoothsdk.cms.PerActivity;
import com.hwug.tikbytebluetoothsdk.view.adapter.IAdapterCallback;

import java.util.List;

import javax.inject.Inject;

@PerActivity
public class DebugDataAdapterCallback extends RecyclerView.Adapter<DebugDataAdapterCallback.SdkDataHolder>
        implements IAdapterCallback {

  private List<String> stringList;
  private final LayoutInflater layoutInflater;

  @Inject
  public DebugDataAdapterCallback(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public void setStringList(List<String> stringList){
    this.stringList = stringList;
  }

  @Override
  public int getItemCount() {
    return (this.stringList != null) ? this.stringList.size() : 0;
  }

  @Override
  @NonNull
  public SdkDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.data_adapter, parent, false);
    return new SdkDataHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull SdkDataHolder holder, final int position) {
    holder.tv_sdk_data.setText(stringList.size() > position ? stringList.get(position) + ", "+position : "error");
  }

  @Override
  public long getItemId(int position) {
    return position;
  }


  static class SdkDataHolder extends RecyclerView.ViewHolder {
    TextView tv_sdk_data;
    SdkDataHolder(View itemView) {
      super(itemView);
      if (itemView!=null){
        tv_sdk_data = itemView.findViewById(R.id.tv_sdk_data);
      }
    }
  }
}
