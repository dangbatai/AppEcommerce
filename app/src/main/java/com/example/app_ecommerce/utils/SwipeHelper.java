package com.example.app_ecommerce.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {
    private int swipedPosition = -1;
    private final Map<Integer, List<UnderlayButton>> buttonBuffer = new HashMap<>();
    private final Queue<Integer> recoverQueue = new LinkedList<>();
    private final RecyclerView recyclerView;
    private final Context context;
    public final SwipeListener SwipeListener;
    private final View.OnTouchListener touchListener = (v, event) -> {
        if (swipedPosition < 0) return false;
        v.performClick();
        List<UnderlayButton> buttons = buttonBuffer.get(swipedPosition);
        if (buttons != null) {
            for (UnderlayButton button : buttons) {
                if (button.handle(event)) {
                    break;
                }
            }
        }
        recoverQueue.add(swipedPosition);
        swipedPosition = -1;
        recoverSwipedItem();
        return false;
    };

    public SwipeHelper(Context context, RecyclerView recyclerView, SwipeListener SwipeListener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.context = context;
        this.recyclerView = recyclerView;
        this.recyclerView.setOnTouchListener(touchListener);
        this.SwipeListener=SwipeListener;
    }

    private void recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            Integer position = recoverQueue.poll();
            if (position != null) {
                recyclerView.getAdapter().notifyItemChanged(position);
            }
        }
    }

    private void drawButtons(Canvas canvas, List<UnderlayButton> buttons, View itemView, float dX) {
        float right = itemView.getRight();
        float left = itemView.getLeft();
        if (dX < 0) { // Swiping left
            for (UnderlayButton button : buttons) {
                float width = button.getIntrinsicWidth() / intrinsicWidth(buttons) * Math.abs(dX);
                float buttonLeft = right - width;
                button.draw(canvas, new RectF(buttonLeft, itemView.getTop(), right, itemView.getBottom()));
                right = buttonLeft;
            }
        } else { // Swiping right
            for (UnderlayButton button : buttons) {
                float width = button.getIntrinsicWidth() / intrinsicWidth(buttons) * Math.abs(dX);
                float buttonRight = left + width;
                button.draw(canvas, new RectF(left, itemView.getTop(), buttonRight, itemView.getBottom()));
                left = buttonRight;
            }
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int position = viewHolder.getAdapterPosition();
        View itemView = viewHolder.itemView;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (!buttonBuffer.containsKey(position)) {
                buttonBuffer.put(position, instantiateUnderlayButton(position));
            }
            List<UnderlayButton> buttons = buttonBuffer.get(position);
            if (buttons != null && !buttons.isEmpty()) {
                float maxDX = dX < 0 ? Math.max(-intrinsicWidth(buttons), dX) : Math.min(intrinsicWidth(buttons), dX);
                drawButtons(c, buttons, itemView, maxDX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private float intrinsicWidth(List<UnderlayButton> buttons) {
        float totalWidth = 0.0f;
        for (UnderlayButton button : buttons) {
            totalWidth += button.getIntrinsicWidth();
        }
        return totalWidth;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    public interface SwipeListener {
        void onDeleteConfirmed(int position);
    }
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (swipedPosition != position) {
            recoverQueue.add(swipedPosition);
        }
        swipedPosition = position;

        if (direction == ItemTouchHelper.LEFT) {
            SwipeListener.onDeleteConfirmed(position);
        } else {
            recoverSwipedItem();
        }
    }

//    private void showDeleteConfirmationDialog(int position) {
//        new AlertDialog.Builder(context)
//                .setTitle("Delete ")
//                .setMessage("Are you sure you want to delete this item?")
//                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                    onConfirmed(position);
//                })
//                .setNegativeButton(android.R.string.no, (dialog, which) -> recoverSwipedItem())
//                .show();
//    }

    protected abstract void onConfirmed(int position);

    public abstract List<UnderlayButton> instantiateUnderlayButton(int position);

    public interface UnderlayButtonClickListener {
        void onClick();
    }


    public static class UnderlayButton {
        private final Context context;
        private final String title;
        private final float textSizeInPixel;
        private final int colorRes;
        private final UnderlayButtonClickListener clickListener;
        private RectF clickableRegion = null;
        private final float horizontalPadding = 50.0f;
        private final float intrinsicWidth;

        public UnderlayButton(Context context, String title, float textSize, @ColorRes int colorRes, UnderlayButtonClickListener clickListener) {
            this.context = context;
            this.title = title;
            this.textSizeInPixel = textSize * context.getResources().getDisplayMetrics().density; // dp to px
            this.colorRes = colorRes;
            this.clickListener = clickListener;

            Paint paint = new Paint();
            paint.setTextSize(this.textSizeInPixel);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextAlign(Paint.Align.LEFT);
            Rect titleBounds = new Rect();
            paint.getTextBounds(title, 0, title.length(), titleBounds);
            this.intrinsicWidth = titleBounds.width() + 2 * horizontalPadding;
        }

        public float getIntrinsicWidth() {
            return intrinsicWidth;
        }

        public void draw(Canvas canvas, RectF rect) {
            Paint paint = new Paint();

            // Draw background
            paint.setColor(ContextCompat.getColor(context, colorRes));
            canvas.drawRect(rect, paint);

            // Draw title
            paint.setColor(ContextCompat.getColor(context, android.R.color.white));
            paint.setTextSize(textSizeInPixel);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextAlign(Paint.Align.LEFT);

            Rect titleBounds = new Rect();
            paint.getTextBounds(title, 0, title.length(), titleBounds);

            float y = rect.height() / 2 + titleBounds.height() / 2 - titleBounds.bottom;
            canvas.drawText(title, rect.left + horizontalPadding, rect.top + y, paint);

            clickableRegion = rect;
        }

        public boolean handle(MotionEvent event) {
            if (clickableRegion != null && clickableRegion.contains(event.getX(), event.getY())) {
                clickListener.onClick();
                return true;
            }
            return false;
        }
    }
}
