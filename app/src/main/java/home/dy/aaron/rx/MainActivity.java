package home.dy.aaron.rx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
  public static final String TAG = "[dyhome]";
  @BindView(R.id.hello_text) TextView helloText;
  @BindView(R.id.stock_updates_recycler_view) RecyclerView recyclerView;

  private LinearLayoutManager layoutManager;
  private StockDataAdapter stockDataAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    recyclerView.setHasFixedSize(true);

    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    stockDataAdapter = new StockDataAdapter();
    recyclerView.setAdapter(stockDataAdapter);

    Observable.just(
        new StockUpdate("GOOGLE", new BigDecimal(12.43), new Date()),
        new StockUpdate("APPL", new BigDecimal(645.1), new Date()),
        new StockUpdate("TWTR", new BigDecimal(1.43), new Date())
        )
        .subscribe(stockDataAdapter::add);

   //
    Observable.just("First item", "Second item")
        .subscribeOn(Schedulers.io())
        .doOnNext(e -> Log.d(TAG, "on-next:" + Thread.currentThread().getName() + ":" + e))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(e -> Log.d(TAG, "subscribe:" + Thread.currentThread().getName() + ":" + e));

//    Observable.just("One", "Two")
//        .subscribeOn(Schedulers.io())
//        .doOnDispose(() -> log("doOnDispose"))
//        .doOnComplete(() -> log("doOnComplete"))
//        .
  }

  private void log(String stage, String item) {
    Log.d(TAG, stage + ":" + Thread.currentThread().getName() + ":" + item);
  }

  private void log(String stage) {
    Log.d(TAG, stage + ":" + Thread.currentThread().getName());
  }


}

