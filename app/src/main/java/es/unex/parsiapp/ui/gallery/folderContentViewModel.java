package es.unex.parsiapp.ui.gallery;

        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;
        import androidx.lifecycle.ViewModel;

public class folderContentViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public folderContentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is folder content fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}