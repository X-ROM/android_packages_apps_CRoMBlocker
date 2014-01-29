/*
 * Copyright (C) 2013 MazWoz Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crom.hfm;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class mainFragment extends Fragment{
	
	private File defHosts;
	private File altHosts;
	private File hosts;
	private SharedPreferences settings;
	@SuppressWarnings("unused")
	private SharedPreferences.Editor editor;
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
		 settings = this.getActivity().getPreferences(Context.MODE_PRIVATE);
		 editor = settings.edit();
		 defHosts = new File("/etc/hosts.og");
		 altHosts = new File("/etc/hosts.alt");
		 hosts = new File("/etc/hosts");
		 View mainView = inflater.inflate(R.layout.fragment_main, container, false);
		 
		 final Switch sw = (Switch)mainView.findViewById(R.id.switch1);
		 if (settings != null) {
			 if (settings.getString("default", null) == "true")
			 {
				 sw.setChecked(true);
			 }
			 else if (settings.getString("default", null) == null) {
				 sw.setChecked(true);
			 }
			 else
			 {
				 sw.setChecked(false);
			 }
		 }
		 else {
			 sw.setChecked(true);
		 }
		 sw.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				try {
					RunAsRoot("mount -o remount, rw /system");
					if (isChecked){
						 if (hosts.exists() && defHosts.exists()) {
							 try {
								 hosts.delete();
								 copyFiles(defHosts);
								 settings.edit().putString("default", "true").commit();
							 }
							 catch (IOException e) {
								 e.printStackTrace();
							 }
							 
						 } else {
							 Toast.makeText(getActivity(), "You do not have the alternate hosts file", Toast.LENGTH_LONG).show();
							 sw.setChecked(false);
						 }
					 }
					else {
						if (hosts.exists() && altHosts.exists()) {
							try {
								 hosts.delete();
								 copyFiles(altHosts);
								 settings.edit().putString("default", "false").commit();
							 }
							 catch (IOException e) {
								 e.printStackTrace();
							 }
						} else {
							Toast.makeText(getActivity(), "You do not have the alternate hosts file", Toast.LENGTH_LONG).show();
							sw.setChecked(true);
						}
					}
				}
				catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		 });
		 
		 return mainView;
	 }

	public void RunAsRoot(String string) throws IOException {
		 Process P = Runtime.getRuntime().exec("su");
		 DataOutputStream os = new DataOutputStream(P.getOutputStream());
		 os.writeBytes(string + "\n");
		 os.writeBytes("exit\n");
		 os.flush();
	 }
	 
	 public void copyFiles(File srcFile) throws IOException {
		 try {
			 RunAsRoot("cp " + srcFile.getAbsolutePath() + " " + hosts.getAbsolutePath());
		 }
		 catch (IOException e) {
			 e.printStackTrace();
		 }
	 }
}
