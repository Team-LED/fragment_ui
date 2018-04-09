package database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.sql.Blob;

@Entity (tableName = "recording_table")
public class RecordingEntity {

    @PrimaryKey (autoGenerate = true)
    private int recordingId;


    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "length")
    private String length;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "img_file")
    private String imgFile;

    @ColumnInfo (name = "title")
    private String title;

    @ColumnInfo (name = "photo_blob")
    private Blob photoBlob;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgFile() {
        return imgFile;
    }

    public void setImgFile(String imgFile) {
        this.imgFile = imgFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Blob getPhotoBlob() {
        return photoBlob;
    }

    public void setPhotoBlob(Blob photoBlob) {
        this.photoBlob = photoBlob;
    }



}
