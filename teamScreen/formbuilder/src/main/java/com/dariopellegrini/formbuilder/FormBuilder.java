package com.dariopellegrini.formbuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dariopellegrini on 19/06/2017.
 */

public class FormBuilder {
    Activity context;
    LinearLayout linearLayout;

    public LinkedHashMap<String, FormElement> formMap;
    public LinkedHashMap<String, View> viewMap;
    public LinkedHashMap<String, View> editTextMap;

    EditText selectedEditText;
    FormElement selectedFormElement;

    private Calendar calendar;

    public FormBuilder(Activity context, LinearLayout linearLayout) {
        this.context = context;
        this.linearLayout = linearLayout;
        calendar = Calendar.getInstance();
        formMap = new LinkedHashMap<String, FormElement>();
        viewMap = new LinkedHashMap<String, View>();
        editTextMap = new LinkedHashMap<String, View>();
    }

    public void build(List<FormObject> formObjects) {
        for (FormObject formObject : formObjects) {
            if (formObject instanceof FormHeader) {
                addToLinearLayout(buildHeader((FormHeader) formObject), ((FormHeader) formObject).getParams());
            } else if (formObject instanceof FormElement) {
                String tag = ((FormElement) formObject).getTagOrToString();
                formMap.put(tag, (FormElement) formObject);
                addToLinearLayout(buildElement((FormElement) formObject), ((FormElement) formObject).getParams());
            } else if (formObject instanceof FormButton) {
                addToLinearLayout(buildButton((FormButton) formObject), ((FormButton) formObject).params);
            }
        }
    }

    // Builders
    private View buildHeader(FormHeader header) {
        TextView headerTextView = new TextView(context, null, android.R.attr.listSeparatorTextViewStyle);
        headerTextView.setText(header.getTitle());
        return headerTextView;
    }

    private View buildElement(final FormElement formElement) {
        FormElement.Type type = formElement.getType();
        ViewGroup textInputLayout = null;
        FineEditText editText = (FineEditText) context.getLayoutInflater().inflate(R.layout.my_edit_text, null, false);
//        EditText editText = new EditText(context, null, R.style.MyEditText);textCursorDrawable
        if (null != formElement.getBackgroundDrawable()) {
            editText.setBackground(formElement.getBackgroundDrawable());
        }
        editText.setTextSize(formElement.getTextSize());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                formElement.setValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        switch (type) {
            case TEXT:
//                textInputLayout = new TextInputLayout(context);
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                editText.setEnabled(formElement.getEnabled());
                editText.setHint(formElement.getHint());
                editText.setText(formElement.getValue());
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setText(formElement.getValue());
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                editTextMap.put(formElement.getTagOrToString(), editText);
                addViewToView(textInputLayout, editText, formElement.getRequired());
                return textInputLayout;
            case TEXTVIEW:
                editText.setEnabled(formElement.getEnabled());
                editText.setHint(formElement.getHint());
                editText.setText(formElement.getValue());
                editText.setGravity(Gravity.LEFT | Gravity.TOP);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setText(formElement.getValue());
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                editTextMap.put(formElement.getTagOrToString(), editText);
                return editText;
            case EMAIL:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                editText.setEnabled(formElement.getEnabled());
                editText.setHint(formElement.getHint());
                editText.setText(formElement.getValue());
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                editText.setText(formElement.getValue());
                editTextMap.put(formElement.getTagOrToString(), editText);
                addViewToView(textInputLayout, editText, formElement.getRequired());
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                return textInputLayout;
            case PHONE:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                editText.setEnabled(formElement.getEnabled());
                editText.setHint(formElement.getHint());
                editText.setText(formElement.getValue());
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                editText.setText(formElement.getValue());
                editTextMap.put(formElement.getTagOrToString(), editText);
                addViewToView(textInputLayout, editText, formElement.getRequired());
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                return textInputLayout;
            case PASSWORD:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                editText.setEnabled(formElement.getEnabled());
                editText.setHint(formElement.getHint());
                editText.setText(formElement.getValue());
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editText.setText(formElement.getValue());
                editTextMap.put(formElement.getTagOrToString(), editText);
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                addViewToView(textInputLayout, editText, formElement.getRequired());
                return textInputLayout;
            case NUMBER:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                editText.setEnabled(formElement.getEnabled());
                editText.setHint(formElement.getHint());
                editText.setText(formElement.getValue());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setText(formElement.getValue());
                editTextMap.put(formElement.getTagOrToString(), editText);
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                addViewToView(textInputLayout, editText, formElement.getRequired());
                return textInputLayout;
            case URL:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                editText.setEnabled(formElement.getEnabled());
                editText.setHint(formElement.getHint());
                editText.setText(formElement.getValue());
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                editText.setText(formElement.getValue());
                editTextMap.put(formElement.getTagOrToString(), editText);
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                addViewToView(textInputLayout, editText, formElement.getRequired());
                return textInputLayout;
            case ZIP:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                editText.setEnabled(formElement.getEnabled());
                editText.setHint(formElement.getHint());
                editText.setText(formElement.getValue());
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                editText.setText(formElement.getValue());
                editTextMap.put(formElement.getTagOrToString(), editText);
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                addViewToView(textInputLayout, editText, formElement.getRequired());
                return textInputLayout;
            case DATE:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                final EditText dateEditText = new EditText(context);
                dateEditText.setTextSize(formElement.getTextSize());
                dateEditText.setFocusable(false);
                dateEditText.setClickable(true);
                dateEditText.setEnabled(formElement.getEnabled());
                dateEditText.setHint(formElement.getHint());
                dateEditText.setText(formElement.getValue());
                dateEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                dateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedFormElement = formElement;
                        pickDate(dateEditText);
                    }
                });
                dateEditText.setText(formElement.getValue());
                editTextMap.put(formElement.getTagOrToString(), editText);
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                addViewToView(textInputLayout, dateEditText, formElement.getRequired());
                return textInputLayout;
            case TIME:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                final EditText timeEditText = new EditText(context);
                timeEditText.setTextSize(formElement.getTextSize());
                timeEditText.setFocusable(false);
                timeEditText.setClickable(true);
                timeEditText.setEnabled(formElement.getEnabled());
                timeEditText.setHint(formElement.getHint());
                timeEditText.setText(formElement.getValue());
                timeEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                timeEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedFormElement = formElement;
                        pickTime(timeEditText);
                    }
                });
                timeEditText.setText(formElement.getValue());
                editTextMap.put(formElement.getTagOrToString(), editText);
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                addViewToView(textInputLayout, timeEditText, formElement.getRequired());
                return textInputLayout;
            case MULTIPLE_SELECTION:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
                final EditText multipleSelectionEditText = new EditText(context);
                multipleSelectionEditText.setTextSize(formElement.getTextSize());
                multipleSelectionEditText.setFocusable(false);
                multipleSelectionEditText.setClickable(true);
                multipleSelectionEditText.setEnabled(formElement.getEnabled());
                multipleSelectionEditText.setHint(formElement.getHint());
                multipleSelectionEditText.setText(formElement.getValue());
                multipleSelectionEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                multipleSelectionEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickMultipleDialog(multipleSelectionEditText, formElement);
                    }
                });
                multipleSelectionEditText.setText(formElement.getValue());
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                addViewToView(textInputLayout, multipleSelectionEditText, formElement.getRequired());
                return textInputLayout;
            case SELECTION:
                textInputLayout = (ViewGroup) context.getLayoutInflater().inflate(R.layout.my_text_input_layout, null, false);
//                final EditText selectionEditText = new EditText(context);
                final FineEditText selectionEditText = (FineEditText) context.getLayoutInflater().inflate(R.layout.my_edit_text_choose, null, false);
                if (null != formElement.getBackgroundDrawable()) {
                    selectionEditText.setBackground(formElement.getBackgroundDrawable());
                }
                selectionEditText.setTextSize(formElement.getTextSize());
                selectionEditText.setFocusable(false);
                selectionEditText.setClickable(true);
                selectionEditText.setEnabled(formElement.getEnabled());
                selectionEditText.setHint(formElement.getHint());
                selectionEditText.setText(formElement.getValue());
                selectionEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                selectionEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickDialog(selectionEditText, formElement);
                    }
                });
                selectionEditText.setText(formElement.getValue());
                viewMap.put(formElement.getTagOrToString(), textInputLayout);
                addViewToView(textInputLayout, selectionEditText, formElement.getRequired());
                return textInputLayout;
            default:
                return null;
        }
    }

    private View buildButton(final FormButton formButton) {
//        Button button = new Button(context);
        FineTextView button = (FineTextView) context.getLayoutInflater().inflate(R.layout.my_button, null, false);
        button.setText(formButton.title);
        button.setTextSize(Utilities.convertPixelsToDp(context.getResources().getDimension(R.dimen.dp_20), context));
        if (formButton.backgroundColor != null) {
            button.setBackgroundColor(formButton.backgroundColor);
        }
        if (formButton.backgroundResource != null) {
            button.setBackgroundResource(formButton.backgroundResource);
        }

        if (formButton.getTextColor() != null) {
            button.setTextColor(formButton.getTextColor());
        }

        if (formButton.runnable != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    formButton.runnable.run();
                }
            });
        }

        return button;
    }

    private void addToLinearLayout(View view, LinearLayout.LayoutParams params) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(8, 8, 8, 8);
        view.setLayoutParams(params != null ? params : layoutParams);

        linearLayout.addView(view);
    }

    private void addViewToView(ViewGroup parent, View child, boolean required) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        child.setLayoutParams(layoutParams);
        TextInputLayout text_input = (TextInputLayout) parent.findViewById(R.id.text_input);
        ImageView mandatory = (ImageView) parent.findViewById(R.id.mandatory);
        if (required) {
            mandatory.setVisibility(View.VISIBLE);
        }
        text_input.addView(child);
    }

    // Date picker
    public void pickDate(EditText et) {
        if (et != null) {
            selectedEditText = et;
            new DatePickerDialog(context, datePickerListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat(selectedFormElement.getDateFormat());

            if (selectedEditText != null) {
                selectedEditText.setText(sdf.format(calendar.getTime()));
                selectedFormElement.setValue(sdf.format(calendar.getTime()));
            }
        }

    };

    // Time picker
    public void pickTime(EditText et) {
        if (et != null) {
            selectedEditText = et;
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(context, timePickerListener, hour, minute, true);//Yes 24 hour time
            mTimePicker.show();
        }
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            calendar.set(Calendar.HOUR, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);

            SimpleDateFormat sdf = new SimpleDateFormat(selectedFormElement.getTimeFormat());

            if (selectedEditText != null) {
                selectedEditText.setText(sdf.format(calendar.getTime()));
                selectedFormElement.setValue(sdf.format(calendar.getTime()));
            }
        }
    };

    // Choice
    private void pickMultipleDialog(final EditText selectedEditText, final FormElement selectedFormElement) {
        final List<String> selectedElements = new ArrayList<String>(selectedFormElement.getOptionsSelected());
        this.selectedEditText = selectedEditText;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("");

        builder.setMultiChoiceItems(selectedFormElement.getOptions().toArray(new CharSequence[selectedFormElement.getOptions().size()]), selectedFormElement.getCheckedValues(),
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            selectedElements.add(selectedFormElement.getOptions().get(which));
                        } else {
                            selectedElements.remove(selectedFormElement.getOptions().get(which));
                        }
                    }
                });
        builder.setPositiveButton(context.getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedFormElement.setOptionsSelected(selectedElements);
                selectedEditText.setText(selectedFormElement.getOptionsSelected().toString());
            }
        });

        builder.setNegativeButton(context.getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private int mPosition = 0;

    private void pickDialog(final EditText selectedEditText, final FormElement selectedFormElement) {
        final List<String> selectedElements = new ArrayList<String>(selectedFormElement.getOptionsSelected());
        this.selectedEditText = selectedEditText;
        SelectedDialog dialog = new SelectedDialog(context, selectedFormElement.getCustomColor(), selectedFormElement.getOptions(), mPosition, new SelectedDialog.MyOnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                mPosition = position;
                selectedElements.clear();
                selectedFormElement.setValue(selectedFormElement.getOptions().get(position));
                selectedElements.add(selectedFormElement.getOptions().get(position));
                selectedFormElement.setOptionsSelected(selectedElements);
                selectedEditText.setText(selectedFormElement.getOptions().get(position));

            }
        });
        dialog.show();
    }

    // Validation
    public boolean validate() {
        boolean isValid = true;

        for (Map.Entry<String, FormElement> pair : formMap.entrySet()) {
            FormElement element = pair.getValue();
            View view = viewMap.get(element.getTagOrToString());
            if (element.getRequired()) {
                if (element.getValue() == null || element.getValue().length() == 0) {
                    isValid = false;
                    if (view instanceof EditText) {
                        ((EditText) view).setError(element.getErrorMessageOrDefault());
                    }
                }
            }

            if (element.getFormValidation() != null) {
                boolean validation = element.getFormValidation().validate();
                if (validation == false) {
                    isValid = validation;
                    if (view instanceof EditText) {
                        ((EditText) view).setError(element.getErrorMessageOrDefault());
                    }
                }
            }
        }
        return isValid;
    }
}
