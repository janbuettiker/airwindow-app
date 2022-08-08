package com.example.airwindow_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

public class Room implements Parcelable {

    @SerializedName("id")
    // id should only be set and never be sent to the backend
    @Expose(deserialize = false)
    private long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;

    /*
        TODO: Solid solution to save image of the room
        As of now, we will just randomly assign an id to the room upon its creation.
        Also do not expose it for gson (de)-serialization
     */
    @Expose(serialize = false, deserialize = false)
    private Integer image;

    protected Room(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        image = in.readInt();
    }

    protected Room() {
        image = new Random().nextInt(3);
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(image);
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Integer getImage() { return image; }

    public void setImage(Integer image) { this.image = image; }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
