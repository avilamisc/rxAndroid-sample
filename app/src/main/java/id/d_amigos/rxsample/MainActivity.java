package id.d_amigos.rxsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lblHello;
    private EditText txtSearch;
    private Button btn1, btn2, btn3;
    private PublishSubject<Integer> buttonSubject = PublishSubject.create();
    //private ReplaySubject<Integer> testReplay = ReplaySubject.create(2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSearch = (EditText) findViewById(R.id.txtSearch);
        lblHello = (TextView) findViewById(R.id.lblHello);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        RxTextView.afterTextChangeEvents(txtSearch)
                .debounce(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io()) // schedulers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TextViewAfterTextChangeEvent>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                lblHello.setText(textViewAfterTextChangeEvent.editable().toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        io.reactivex.Observable<Integer> observable =  buttonSubject.distinctUntilChanged()
                .subscribeOn(Schedulers.io()) // schedulers
                .observeOn(AndroidSchedulers.mainThread())
                .share();

        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Toast.makeText(MainActivity.this, ""+integer,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        observable.delay(5, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Toast.makeText(MainActivity.this, "delayed:: "+integer,
                        Toast.LENGTH_SHORT).show();
                //Log.v("TRAINING", integer + "");
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int event;
        if (view.getId() == btn1.getId()) {
            event = 1;
        } else if (view.getId() == btn2.getId()) {
            event = 2;
        } else {
            event = 3;
        }

        buttonSubject.onNext(event);
//        buttonSubject.onError();
//        buttonSubject.onComplete();
    }
}
