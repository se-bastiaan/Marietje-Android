package nl.ru.science.mariedroid.widget;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;

import nl.ru.science.mariedroid.database.MediaEntry;

/**
 * Adapter that exposes data from a Cursor to a ListView widget. The Cursor must include a column named "_id"
 * or this class will not work.
 */
public class AlphabetCursorAdapter extends SimpleCursorAdapter implements SectionIndexer
{

    AlphabetIndexer mAlphabetIndexer;

    public AlphabetCursorAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags)
    {
        super(context, layout, cursor, from, to, flags);

        mAlphabetIndexer = new AlphabetIndexer(cursor, 2, "#ABCDEFGHIJKLMNOPQRTSUVWXYZ");
        mAlphabetIndexer.setCursor(cursor);
    }

    /**
     * Performs a binary search or cache lookup to find the first row that matches a given section's starting letter.
     */
    @Override
    public int getPositionForSection(int sectionIndex)
    {
        return mAlphabetIndexer.getPositionForSection(sectionIndex);
    }

    /**
     * Returns the section index for a given position in the list by querying the item and comparing it with all items
     * in the section array.
     */
    @Override
    public int getSectionForPosition(int position)
    {
        return mAlphabetIndexer.getSectionForPosition(position);
    }

    /**
     * Returns the section array constructed from the alphabet provided in the constructor.
     */
    @Override
    public Object[] getSections()
    {
        return mAlphabetIndexer.getSections();
    }

    public Cursor swapCursor(Cursor c) {
        // Create our indexer
        if (c != null) {
            mAlphabetIndexer = new AlphabetIndexer(c, 2, "#ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        return super.swapCursor(c);
    }
}
