package com.example.adnroid.camera;

import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class Settings extends Activity implements OnClickListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		EditText edit = null;
		edit = (EditText) findViewById(R.id.editText_ip);
		edit.setText(Const.getEcocast_ip(), BufferType.NORMAL);
		edit = (EditText) findViewById(R.id.editText_port);
		edit.setText(Const.getEcocast_port(), BufferType.NORMAL);
		
		edit = (EditText) findViewById(R.id.editText_sec);
		edit.setText(Const.getEcocast_sec(), BufferType.NORMAL);
		
		View showButton = null;
		showButton = findViewById(R.id.settings_ok);
	    showButton.setOnClickListener(this);
		
		// Supported Size
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // アイテムを追加します
        List<Size> supporetedSize = Const.getSupportedSizes();
        Iterator<Size> it = supporetedSize.iterator();
        while(it.hasNext()){
        	Size size = it.next();
        	String str = size.width + "x" + size.height;
        	adapter.add(str);
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // アダプターを設定します
        spinner.setAdapter(adapter);
        
        // set
        spinner.setSelection(Const.getEcocast_sizeindex());
        
	}
	
	@SuppressLint({ "WorldWriteableFiles", "WorldReadableFiles" })
	@Override
    public void onClick(View v) {
        if (v.getId() == R.id.settings_ok){
			SharedPreferences pref = getSharedPreferences("pref",
					MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
			Editor e = pref.edit();
			EditText edit = null;
			
			// ip:port
			edit = (EditText) findViewById(R.id.editText_ip);
			e.putString("ecocast_ip", edit.getText().toString());
			Const.setEcocast_ip(edit.getText().toString());
			edit = (EditText) findViewById(R.id.editText_port);
			e.putString("ecocast_port", edit.getText().toString());
			Const.setEcocast_port(edit.getText().toString());
			
			// size
			List<Size> supporetedSize = Const.getSupportedSizes();
			Spinner spinner = (Spinner) findViewById(R.id.spinner);
			//String item = (String) spinner.getSelectedItem();
			int item = spinner.getSelectedItemPosition();
			//String[] splited = item.split("x");
			e.putInt("ecocast_sizeindex", item);
			Const.setEcocast_sizeindex(item);
			Const.setEcocast_size(supporetedSize.get(item));
			
			// second
			edit = (EditText) findViewById(R.id.editText_sec);
			e.putString("ecocast_sec", edit.getText().toString());
			Const.setEcocast_sec(edit.getText().toString());
			
			e.commit();

        	Toast toast = Toast.makeText(getApplicationContext(),
					"Settings have saved.", Toast.LENGTH_SHORT);
			toast.show();
			
			this.finish();
        }
    }
}