package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class history extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    public String UID;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.history_list);

        Query query = firebaseFirestore.collection("game").whereEqualTo("User", UID).orderBy("Ended");
        FirestoreRecyclerOptions<previousGames> options = new FirestoreRecyclerOptions.Builder<previousGames>()
                .setQuery(query, previousGames.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<previousGames, historyViweHolder>(options) {
            @NonNull
            @Override
            public historyViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
                return new historyViweHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull historyViweHolder holder, int position, @NonNull previousGames model) {
                holder.date.setText(model.getEnded());
                holder.bet.setText("$"+model.getBetValue());
                holder.dealerTotal.setText("Dealers Hand: "+(int)model.getDealerHand());
                if (model.isPlayerWon()) {holder.win.setText("Result: Won");}
                else {holder.win.setText("Result: Lost");}
                holder.playerTotal.setText("Player Hand: "+(int)model.getPlayerHand());
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

    }

    private class historyViweHolder extends RecyclerView.ViewHolder {
        private TextView date, bet, win, playerTotal, dealerTotal;

        public historyViweHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txtDate);
            bet = itemView.findViewById(R.id.txtBet);
            win = itemView.findViewById(R.id.txtWin);
            playerTotal = itemView.findViewById(R.id.txtPlayerTotal);
            dealerTotal = itemView.findViewById(R.id.txtDealerTotal);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}