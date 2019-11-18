package com.example.registrazioneapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<Users> {
    private Activity context;
    private List<Users> usersList;

    public UserAdapter(Activity context,List<Users>usersList){
        super(context,R.layout.list_view,usersList);
        this.context = context;
        this.usersList = usersList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.list_view,null,true);

        TextView motivo = listView.findViewById(R.id.Motivo);
        TextView data = listView.findViewById(R.id.Data);

        Users users = usersList.get(position);

        motivo.setText(users.getMotivo());
        data.setText(users.getData());

        return listView;

    }

}
