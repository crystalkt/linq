package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public class LastTest extends EnumerableTest {
    private static <T> IEnumerable<T> EmptySource() {
        return Linq.empty();
    }

    private static <T> void TestEmptyNotIList() {
        IEnumerable<T> source = EmptySource();

        Assert.assertNull(as(source, IList.class));
        assertThrows(InvalidOperationException.class, () -> source.runOnce().last());
    }

    @Test
    public void SameResultsRepeatCallsIntQuery() {
        IEnumerable<Integer> q = Linq.asEnumerable(new int[]{9999, 0, 888, -1, 66, -777, 1, 2, -12345})
                .where(x -> x > Integer.MIN_VALUE);

        Assert.assertEquals(q.last(), q.last());
    }

    @Test
    public void SameResultsRepeatCallsStringQuery() {
        IEnumerable<String> q = Linq.asEnumerable("!@#$%^", "C", "AAA", "", "Calling Twice", "SoS", Empty)
                .where(x -> !IsNullOrEmpty(x));

        Assert.assertEquals(q.last(), q.last());
    }

    private <T> void TestEmptyIList() {
        IEnumerable<T> source = Linq.asEnumerable(Collections.EMPTY_LIST);

        Assert.assertNotNull(as(source, IList.class));
        assertThrows(InvalidOperationException.class, () -> source.runOnce().last());
    }

    @Test
    public void EmptyIListT() {
        this.<Integer>TestEmptyIList();
        this.<String>TestEmptyIList();
        this.<Date>TestEmptyIList();
        this.<LastTest>TestEmptyIList();
    }

    @Test
    public void IListTOneElement() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{5});
        int expected = 5;

        Assert.assertNotNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.last());
    }

    @Test
    public void IListTManyElementsLastIsDefault() {
        IEnumerable<Integer> source = Linq.asEnumerable(-10, 2, 4, 3, 0, 2, null);
        Integer expected = null;

        assertIsAssignableFrom(IList.class, source);
        Assert.assertEquals(expected, source.last());
    }

    @Test
    public void IListTManyElementsLastIsNotDefault() {
        IEnumerable<Integer> source = Linq.asEnumerable(-10, 2, 4, 3, 0, 2, null, 19);
        Integer expected = 19;

        assertIsAssignableFrom(IList.class, source);
        Assert.assertEquals(expected, source.last());
    }

    @Test
    public void EmptyNotIListT() {
        LastTest.<Integer>TestEmptyNotIList();
        LastTest.<String>TestEmptyNotIList();
        LastTest.<Date>TestEmptyNotIList();
        LastTest.<LastTest>TestEmptyNotIList();
    }

    @Test
    public void OneElementNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(-5, 1);
        int expected = -5;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.last());
    }

    @Test
    public void ManyElementsNotIListT() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(3, 10);
        int expected = 12;

        Assert.assertNull(as(source, IList.class));
        Assert.assertEquals(expected, (int) source.last());
    }

    @Test
    public void IListEmptySourcePredicate() {
        IEnumerable<Integer> source = Linq.asEnumerable(Collections.EMPTY_LIST);

        assertThrows(InvalidOperationException.class, () -> source.last(x -> true));
        assertThrows(InvalidOperationException.class, () -> source.last(x -> false));
    }

    @Test
    public void OneElementIListTruePredicate() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{4}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 4;

        Assert.assertEquals(expected, (int) source.last(predicate));
    }

    @Test
    public void ManyElementsIListPredicateFalseForAll() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{9, 5, 1, 3, 17, 21}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;

        assertThrows(InvalidOperationException.class, () -> source.last(predicate));
    }

    @Test
    public void IListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{9, 5, 1, 3, 17, 21, 50}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 50;

        Assert.assertEquals(expected, (int) source.last(predicate));
    }

    @Test
    public void IListPredicateTrueForSome() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 18;

        Assert.assertEquals(expected, (int) source.last(predicate));
    }

    @Test
    public void IListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = Linq.asEnumerable(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9});
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 18;

        Assert.assertEquals(expected, (int) source.runOnce().last(predicate));
    }

    @Test
    public void NotIListIListEmptySourcePredicate() {
        IEnumerable<Integer> source = Linq.range(1, 0);

        assertThrows(InvalidOperationException.class, () -> source.last(x -> true));
        assertThrows(InvalidOperationException.class, () -> source.last(x -> false));
    }

    @Test
    public void OneElementNotIListTruePredicate() {
        IEnumerable<Integer> source = NumberRangeGuaranteedNotCollectionType(4, 1);
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 4;

        Assert.assertEquals(expected, (int) source.last(predicate));
    }

    @Test
    public void ManyElementsNotIListPredicateFalseForAll() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{9, 5, 1, 3, 17, 21}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;

        assertThrows(InvalidOperationException.class, () -> source.last(predicate));
    }

    @Test
    public void NotIListPredicateTrueOnlyForLast() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{9, 5, 1, 3, 17, 21, 50}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 50;

        Assert.assertEquals(expected, (int) source.last(predicate));
    }

    @Test
    public void NotIListPredicateTrueForSome() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 18;

        Assert.assertEquals(expected, (int) source.last(predicate));
    }

    @Test
    public void NotIListPredicateTrueForSomeRunOnce() {
        IEnumerable<Integer> source = ForceNotCollection(Linq.asEnumerable(new int[]{3, 7, 10, 7, 9, 2, 11, 18, 13, 9}));
        Func1<Integer, Boolean> predicate = EnumerableTest::IsEven;
        int expected = 18;

        Assert.assertEquals(expected, (int) source.runOnce().last(predicate));
    }

    @Test
    public void NullSource() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).last());
    }

    @Test
    public void NullSourcePredicateUsed() {
        assertThrows(NullPointerException.class, () -> ((IEnumerable<Integer>) null).last(i -> i != 2));
    }

    @Test
    public void NullPredicate() {
        Func1<Integer, Boolean> predicate = null;
        assertThrows(ArgumentNullException.class, () -> Linq.range(0, 3).last(predicate));
    }

    @Test
    public void testLast() {
        IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch"));
        Assert.assertEquals("mitch", enumerable.last());

        IEnumerable emptyEnumerable = Linq.asEnumerable(Collections.emptyList());
        try {
            emptyEnumerable.last();
            Assert.fail("should not run at here");
        } catch (InvalidOperationException ignored) {
        }

        IEnumerable<String> enumerable2 = Linq.asEnumerable(Collections.unmodifiableCollection(Arrays.asList("jimi", "noel", "mitch")));
        Assert.assertEquals("mitch", enumerable2.last());
    }

    @Test
    public void testLastWithPredicate() {
        IEnumerable<String> enumerable = Linq.asEnumerable(Arrays.asList("jimi", "mitch", "ming"));
        Assert.assertEquals("mitch", enumerable.last(x -> x.startsWith("mit")));
        try {
            enumerable.last(x -> false);
            Assert.fail();
        } catch (InvalidOperationException ignored) {
        }

        IEnumerable<String> emptyEnumerable = Linq.asEnumerable(Collections.emptyList());
        try {
            emptyEnumerable.last(x -> {
                Assert.fail("should not run at here");
                return false;
            });
            Assert.fail();
        } catch (InvalidOperationException ignored) {
        }
    }
}
