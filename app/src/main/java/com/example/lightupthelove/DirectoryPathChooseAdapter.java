package com.example.lightupthelove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryPathChooseAdapter extends RecyclerView.Adapter<DirectoryPathChooseAdapter.DirectoryPathHolder> {

    private List<File> directoryList;
    private Context context;
    private OperationListener listener;
    private static final String DEFAULT_PATH = "/storage/emulated/0";//default path
    private String currentParentFilePath = DEFAULT_PATH;//Record the path of the parent folder of the current list, default root path (used when clicking back to the previous level)
    private boolean canOtherChoose = true;//Is it selectable (you can't select a folder once it's selected)
    private int choosePosition = -1;//Selected position

    //The usage of Recyclerview is inspired from a tutorial.
    //Reference URL: https://www.jianshu.com/p/5ad99a1170ab

    //The folder choose is inspired from a tutorial. The tutorial is just a revelation, on which I have made very significant changes
    //Reference URL: https://blog.csdn.net/sinat_25689603/article/details/51887756
    public DirectoryPathChooseAdapter(Context context, OperationListener listener) {
        this.context = context;
        this.listener = listener;
        getFileDir(DEFAULT_PATH);//Default path to load
    }

    @NonNull
    @Override
    public DirectoryPathHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_directory_path, parent, false);
        return new DirectoryPathHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull DirectoryPathHolder holder, @SuppressLint("RecyclerView") int position) {
        final File file = directoryList.get(position);
        holder.tvDirectoryName.setText(file.getName());
        holder.llDirectory.setOnClickListener(v ->
        {
            // Click on the next level to take the address of this level as the upper path after the current operation (the file currently taken is the file before the operation)
            if (listener != null && !canOtherChoose) {
                listener.forbidJump();
            } else {
                currentParentFilePath = file.getPath();
                getFileDir(file.getAbsolutePath());
            }
        });
        holder.ivFile.setBackground(context.getResources().getDrawable(R.drawable.directory));
        holder.cbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (canOtherChoose) {//Locate to the selected position and assign it to the choosePosition
                        choosePosition = position;
                        canOtherChoose = false;
                    } else {
                        //Callbacks
                        holder.cbChoose.setChecked(false);
                        listener.forbidChoose();
                    }
                } else {
                    //If it changes from selected to unselected (it can actually be treated as unselected)
                    choosePosition = -1;
                    canOtherChoose = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return directoryList.size();
    }


    static class DirectoryPathHolder extends RecyclerView.ViewHolder {

        private LinearLayout llDirectory;
        private ImageView ivFile;
        private TextView tvDirectoryName;
        private CheckBox cbChoose;

        public DirectoryPathHolder(@NonNull View itemView) {
            super(itemView);
            llDirectory = (LinearLayout) itemView.findViewById(R.id.llDirectory);
            ivFile = (ImageView) itemView.findViewById(R.id.ivFile);
            tvDirectoryName = (TextView) itemView.findViewById(R.id.tvDirectoryName);
            cbChoose = (CheckBox) itemView.findViewById(R.id.cbChoose);
        }
    }

    //Back to root directory
    public void goToRootPath() {
        if (listener != null && !canOtherChoose) {
            listener.forbidJump();
        } else {
            getFileDir(DEFAULT_PATH);
        }
    }


     //Back to parent list
    public void gotoLast() {
        if (listener != null && !canOtherChoose) {
            listener.forbidJump();
        } else {
            if (!currentParentFilePath.equals(DEFAULT_PATH)) {
                //This time it should take the file in the upper path of the variable address file
                getFileDir(new File(currentParentFilePath).getParentFile().getPath());
                //Then use the path of the upper layer as the upper layer path after this operation
                currentParentFilePath = new File(currentParentFilePath).getParentFile().getPath();
            }
        }
    }


     //Get all the folders under the selected file path and update them to the listview
    private void getFileDir(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        directoryList = new ArrayList<>();
        if (files != null) {
            for (File value : files) {
                //Select the folder and not . that starts with
                if (value.isDirectory() && !value.getName().startsWith(".")) {
                    directoryList.add(value);
                }
            }


            //Sort by name
            //Java iterates through all the files in a folder listFiles() to achieve the ascending order by name implementation from a tutorial reference.
            //Reference URL: http://t.zoukankan.com/pxblog-p-14715537.html
            Collections.sort(directoryList, (o1, o2) -> {
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                return o1.getName().compareTo(o2.getName());
            });
        }
        notifyDataSetChanged();
        choosePosition = -1;
    }

    //Function to be able to select again
    public boolean canChoose() {
        return !canOtherChoose;
    }

    public File getChooseDirectory() {
        return directoryList.get(choosePosition);
    }

    public interface OperationListener {

        void forbidJump();

        void forbidChoose();
    }


}
