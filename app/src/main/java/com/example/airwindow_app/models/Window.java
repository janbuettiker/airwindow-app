package com.example.airwindow_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Window implements Parcelable {

    @SerializedName("id")
    // id should only be set and never be sent to the backend
    @Expose(serialize = false)
    private long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("currentState")
    @Expose
    private String currentState;
    @SerializedName("desiredState")
    @Expose
    private String desiredState;
    // Boolean would be nicer but requires API lvl 29 for Parcelable
    @SerializedName("weatherAware")
    @Expose
    private String weatherAware;
    // Used for multi select recycler view, so no need to import or export to backend
    @Expose(serialize = false, deserialize = false)
    private boolean isChecked;

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(String desiredState) {
        this.desiredState = desiredState;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherAware() { return weatherAware; }

    public void setWeatherAware(String weatherAware) { this.weatherAware = weatherAware; }

    public boolean isChecked() {return isChecked; }

    public void setChecked(boolean checked) {isChecked = checked; }

    /*
                    Parcelable implementation to be able to send the whole object
                    through an intent for easier data exchange between activites.
                 */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(currentState);
        dest.writeString(desiredState);
        dest.writeString(weatherAware);
    }

    public static final Parcelable.Creator<Window> CREATOR = new Parcelable.Creator<Window>() {
        public Window createFromParcel(Parcel in) {
            return new Window(in);
        }

        public Window[] newArray(int size) {
            return new Window[size];
        }
    };

    /*
        Constructor for Parcelable
        Attention! FIFO, so writeToParcel and the constructor must
        read and write in the same order
     */
    protected Window(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        currentState = in.readString();
        desiredState = in.readString();
        weatherAware = in.readString();
    }

    /*
        Default constructor needs to be created because the
        Parcelable constructor voids the non-written default
     */
    public Window() {

    }

    @Override
    public String toString() {
        return "Window{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", currentState='" + currentState + '\'' +
                ", desiredState='" + desiredState + '\'' +
                ", weatherAware='" + weatherAware + '\'' +
                '}';
    }
}
