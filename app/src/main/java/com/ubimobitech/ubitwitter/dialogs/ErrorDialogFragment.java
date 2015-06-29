/*
 * Copyright 2015, Isaac Ben-Akiva
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
/**
 * FILE: ErrorDialogFragment.java
 * AUTHOR: Dr. Isaac Ben-Akiva <isaac.ben-akiva@ubimobitech.com>
 * <p/>
 * CREATED ON: 28/06/15
 */

package com.ubimobitech.ubitwitter.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.ubimobitech.ubitwitter.R;

/**
 * Created by benakiva on 28/06/15.
 */
public class ErrorDialogFragment extends DialogFragment {
    private static final String ERROR_MSG_ARG = "error_msg_arg";

    private String mMsg;

    public static ErrorDialogFragment newInstance(final String message) {
        Bundle args = new Bundle();
        args.putString(ERROR_MSG_ARG, message);

        ErrorDialogFragment fragment = new ErrorDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public ErrorDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {
            mMsg = args.getString(ERROR_MSG_ARG);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMsg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().dismiss();
                    }
                });

        return builder.create();
    }
}
