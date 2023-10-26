package com.example.saper;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saper.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random random = new Random();

    Button[][] cells;

    final int WIDTH = 10;
    final int HEIGHT = 15;

    TextView mines;

    final int MINESCONST = random.nextInt(37 - 25) + 25;

    boolean[][] minecoords = new boolean[15][10];

    boolean[][] mineclicklong = new boolean[15][10];

    boolean[][] mineclick = new boolean[15][10];

    boolean firsttime = true;

    int[][] minecountarr = new int[15][10];

    int MinesCurren = MINESCONST;

    int[][] mineindex = new int[MINESCONST][2];

    int[][] minec = new int[15][10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mines = findViewById(R.id.mines);
        mines.setText(MinesCurren + " / " + MINESCONST + " \uD83D\uDEA9");
        generate();
    }

    public void openZeros(int x, int y) {
        int tx, ty, tx1, ty1;
        if (x > 0) {
            tx = x - 1;
        } else {
            tx = 0;
        }
        if (y > 0) {
            ty = y - 1;
        } else {
            ty = 0;
        }
        if (x == 14) {
            tx1 = 15;
        } else {
            tx1 = x + 2;
        }
        if (y == 9) {
            ty1 = 10;
        } else {
            ty1 = y + 2;
        }
        for (int i = tx; i != tx1; ++i) {
            for (int j = ty; j != ty1; ++j) {
                if (minec[i][j] == 0) {
                    if ((!(minecoords[i][j])) && (!(mineclick[i][j])) && ((Math.abs(x - i) != Math.abs(y - j)) || (x == i && y == j))) {
                        if ((i + j) % 2 != 0) {
                            cells[i][j].setBackgroundColor(0xfffafafa);
                        } else {
                            cells[i][j].setBackgroundColor(0xC8fafafa);
                        }
                        if (minec[i][j] == 0) {
                            cells[i][j].setText("");
                        } else {
                            cells[i][j].setText("" + minec[i][j]);
                        }
                        mineclick[i][j] = true;
                        if (mineclicklong[i][j]) {
                            mineclicklong[i][j] = false;
                            ++MinesCurren;
                        }
                        openZeros(i, j);
                    }
                } else if(!(minecoords[i][j])) {
                    if ((i + j) % 2 != 0) {
                        cells[i][j].setBackgroundColor(0xfffafafa);
                    } else {
                        cells[i][j].setBackgroundColor(0xC8fafafa);
                    }
                    if (minec[i][j] == 0) {
                        cells[i][j].setText("");
                    } else {
                        cells[i][j].setText("" + minec[i][j]);
                    }
                    mineclick[i][j] = true;
                    if (mineclicklong[i][j]) {
                        mineclicklong[i][j] = false;
                        ++MinesCurren;
                    }
                }
            }
        }
        mines.setText(MinesCurren + " / " + MINESCONST + " \uD83D\uDEA9");
    }

    public void generate() {
        GridLayout layout = findViewById(R.id.Grid);
        layout.removeAllViews();
        layout.setColumnCount(WIDTH);

        cells = new Button[HEIGHT][WIDTH];
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        int t = 0;

        while (t != MINESCONST) {
            int x = random.nextInt(15), y = random.nextInt(10);
            if (! minecoords[x][y]) {
                minecoords[x][y] = true;
                mineindex[t][0] = x;
                mineindex[t][1] = y;
                ++t;
            }
        }

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                cells[i][j] = (Button) inflater.inflate(R.layout.cell, layout, false);
                int timer = 0;
                int tx, ty, tx1, ty1;
                if (i > 0) {
                    tx = i - 1;
                } else {
                    tx = 0;
                }
                if (j > 0) {
                    ty = j - 1;
                } else {
                    ty = 0;
                }
                if (i == 14) {
                    tx1 = 15;
                } else {
                    tx1 = i + 2;
                }
                if (j == 9) {
                    ty1 = 10;
                } else {
                    ty1 = j + 2;
                }
                for (int a = tx; a != tx1; ++a) {
                    for (int b = ty; b != ty1; ++b) {
                        if (minecoords[a][b]) {
                            ++timer;
                        }
                    }
                }
                minec[i][j] = timer;
            }
        }

        for (int i = 0; i < HEIGHT; ++i) {
            for (int j = 0; j < WIDTH; ++j) {
                if ((i + j) % 2 == 0) {
                    cells[i][j].setBackgroundColor(0xC860ff38);
                } else {
                    cells[i][j].setBackgroundColor(0xff3dff0d);
                }
//                cells[i][j].setText("" + (minec[i][j]));
                cells[i][j].setText("");
                cells[i][j].setTag("" + (j + HEIGHT * i));
                int finalI = i;
                int finalJ = j;
                cells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (minecoords[finalI][finalJ]) {
                            Toast.makeText(getApplicationContext(), "You lose", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i != 15; ++i) {
                                for (int j = 0; j != 10; ++j) {
                                    if (minecoords[i][j]) {
                                        cells[i][j].setBackgroundColor(Color.RED);
                                        cells[i][j].setText("\uD83D\uDCA3");
                                    }
                                }
                            }
                        } else {
                            if (firsttime) {
                                openZeros(finalI, finalJ);
                                firsttime = false;
                            } else {
                                if (minec[finalI][finalJ] != 0) {
                                    cells[finalI][finalJ].setText("" + minec[finalI][finalJ]);
//                                    openZeros(finalI, finalJ);
                                } else {
                                    openZeros(finalI, finalJ);
                                }
                                if ((finalI + finalJ) % 2 != 0) {
                                    v.setBackgroundColor(0xfffafafa);
                                } else {
                                    v.setBackgroundColor(0xC8fafafa);
                                }
                                mineclick[finalI][finalJ] = true;
                                if (mineclicklong[finalI][finalJ]) {
                                    mineclicklong[finalI][finalJ] = false;
                                    ++MinesCurren;
                                }
                            }
                        }
                        mines.setText(MinesCurren + " / " + MINESCONST + " \uD83D\uDEA9");
                    }
                });
                cells[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!mineclicklong[finalI][finalJ]) {
                            if (MinesCurren > 0) {
                                if (!mineclick[finalI][finalJ]) {
                                    --MinesCurren;
                                    mineclicklong[finalI][finalJ] = true;
                                    cells[finalI][finalJ].setText("\uD83D\uDEA9");
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Not enough flags!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ++MinesCurren;
                            mineclicklong[finalI][finalJ] = false;
                            cells[finalI][finalJ].setText("");
                        }
                        mines.setText(MinesCurren + " / " + MINESCONST + " \uD83D\uDEA9");

                        if (MinesCurren == 0) {
                            boolean flag = true;
                            for (int x = 0; x != 15; ++ x) {
                                for (int y = 0; y != 10; ++y) {
                                    if (mineclicklong[x][y] != minecoords[x][y]) {
                                        flag = false;
                                        break;
                                    }
                                    if (!flag) {
                                        break;
                                    }
                                }
                            }
                            if (flag) {
                                Toast.makeText(getApplicationContext(), "You win!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    }
                });
//                cells[i][j].setBackground();
                layout.addView(cells[i][j]);
            }
        }

//        for (int i = 0; i != MINESCONST; ++i) {
//            cells[mineindex[i][0]][mineindex[i][1]].setBackgroundColor(Color.BLUE);
//        }

    }
}