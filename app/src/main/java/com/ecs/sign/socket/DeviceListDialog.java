package com.ecs.sign.socket;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.ecs.sign.R;
import com.ecs.sign.socket.scan.ServerDeviceEntity;
import com.ecs.sign.socket.scan.ServerDeviceListAdapter;

import java.util.ArrayList;

/**
 * 作者：RedKeyset on 2019/11/18 16:35
 * 邮箱：redkeyset@aliyun.com
 */
public class DeviceListDialog {

    private Context context;
    TextView tvTransportTitle;
    RecyclerView recyclerViewDeviceList;
    TextView btnNo;
    TextView btnYes;
    LinearLayout llBtnContainer;
    private Dialog deviceDialog;
    private ArrayList<ServerDeviceEntity> serverDeviceList = new ArrayList<>();



    private TextView btnConfirmOK;
    private TextView btnConfirmCancel;
    
    private int currentItem;
    private ServerDeviceListAdapter deviceListAdapter;
    private Dialog confirmDialog;

    public DeviceListDialog(Context context) {
        this.context = context;
        initDeviceDialog();
        initConfirmDialog();
    }


    public interface DeviceDialogCallBack {
        void TransportCancel();
        void TransportDeviceSelected(String deviceIp);
    }


    public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener){
        deviceDialog.setOnDismissListener(dismissListener);
    }

    private void initDeviceDialog() {
        deviceDialog = new Dialog(context, R.style.device_list_dialog);

        View view =LayoutInflater.from(context).inflate(R.layout.dialog_socker_server_device_list, null);
        deviceDialog.setContentView(view);
        deviceDialog.setCanceledOnTouchOutside(false);

        tvTransportTitle = view.findViewById(R.id.tv_transport_title);
        tvTransportTitle.setText("请选择要传输的设备");
        llBtnContainer =  view.findViewById(R.id.ll_allow_layout);
        btnNo = view.findViewById(R.id.bt_transport_no);
        btnYes = view.findViewById(R.id.bt_transport_yes);


        recyclerViewDeviceList = view.findViewById(R.id.rv_server_device_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewDeviceList.setLayoutManager(layoutManager);

        deviceListAdapter = new ServerDeviceListAdapter(serverDeviceList);
        recyclerViewDeviceList.setAdapter(deviceListAdapter);
        recyclerViewDeviceList.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.rl_layout_item) {
                    currentItem = position;
                    for (int i = 0; i < serverDeviceList.size(); i++) {
                        boolean isCheck = (i == position) ? true : false;
                        serverDeviceList.get(i).setCheck(isCheck);
                    }
                    deviceListAdapter.notifyDataSetChanged();

                }
            }
        });

        Window window = deviceDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.select_anim);
        WindowManager.LayoutParams lp = window.getAttributes(); // 获取对话框当前的参数值

        lp.width =  context.getResources().getDisplayMetrics().widthPixels; // 设置与屏幕一样宽
        view.measure(0, 0);
        lp.height = view.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        window.setAttributes(lp);
    }


    public void showDeviceDialog(DeviceDialogCallBack dialogCallBack) {
        serverDeviceList.clear();

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallBack.TransportCancel();
                dismiss("取消发送...");
            }
        });


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serverDeviceList.size() > 0) {
                    ServerDeviceEntity serverdeviceEntity = serverDeviceList.get(currentItem);
                    String deviceIp = serverdeviceEntity.getDeviceIp();
                    String deviceName = serverdeviceEntity.getDeviceName();
                    if (!deviceIp.isEmpty()) {
                        dialogCallBack.TransportDeviceSelected(deviceIp);
                        tvTransportTitle.setText("数据将发送给：" + deviceName);
                    } else {
                        Toast.makeText(context, "没有任何设备！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "没有任何设备！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!deviceDialog.isShowing()) {
            deviceDialog.show();
        }
    }

    public void dismiss(String str) {
        tvTransportTitle.setText(str);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                serverDeviceList.clear();
                deviceDialog.dismiss();
            }
        }, 300);
    }

    public void showProgress(String str) {
        tvTransportTitle.setText(str);
    }

    public void addSocketDevice(ServerDeviceEntity entity) {
        serverDeviceList.add(entity);
        deviceListAdapter.notifyDataSetChanged();
    }

    public void dismissAllowBtn() {
        llBtnContainer.setVisibility(View.GONE);
    }
    public void showAllowBtn() {
        llBtnContainer.setVisibility(View.VISIBLE);
    }

    private void initConfirmDialog() {
        confirmDialog = new Dialog(context, R.style.device_list_dialog);
        RelativeLayout root = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
        confirmDialog.setContentView(root);
        confirmDialog.setCanceledOnTouchOutside(false);

        btnConfirmOK = root.findViewById(R.id.bt_allow_determine);
        btnConfirmCancel = root.findViewById(R.id.bt_allow_cancel);


        Window window = confirmDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.select_anim);
        WindowManager.LayoutParams lp = window.getAttributes(); // 获取对话框当前的参数值
//                lp.x = 0; // 新位置X坐标
//                lp.y = -20; // 新位置Y坐标
        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 设置与屏幕一样宽
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        window.setAttributes(lp);
    }



    public interface ConfirmCallBack{
        void onConfirmOK();
        void onConfirmCancel();
    }

    public void showConfirmDialog( ConfirmCallBack confirmCallBack){
        if (confirmCallBack!=null){
            btnConfirmCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                    confirmCallBack.onConfirmCancel();
                }
            });

            btnConfirmOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                    confirmCallBack.onConfirmOK();
                }
            });

        }

        if (!confirmDialog.isShowing()){
            confirmDialog.show();
        }
    }
}
