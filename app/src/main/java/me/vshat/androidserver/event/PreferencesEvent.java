package me.vshat.androidserver.event;

public class PreferencesEvent extends DataEvent<String> {
    public PreferencesEvent(String text) {
        super(text);
    }
}