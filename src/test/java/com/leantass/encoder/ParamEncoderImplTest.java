package com.leantass.encoder;

import static com.leantass.encoder.ParamEncoder.TruncationStyle.INTEGER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Map.Entry;
import java.util.SortedMap;

import com.google.common.collect.ImmutableSortedMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for class {@link ParamEncoderImpl}.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
@RunWith(MockitoJUnitRunner.class)
public class ParamEncoderImplTest {

  @Mock
  private ParamEncoderObject paramEncoderObject;
  @Mock
  private ParamEncoderArray paramEncoderArray;
  private ParamEncoderImpl encoder;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    encoder = new ParamEncoderImpl(paramEncoderObject, paramEncoderArray);
  }

  @Test
  public void shouldEncode() {
    encoder.addFieldTruncationRule("int1", INTEGER, 2);
    encoder.addFieldTruncationRule("int2", INTEGER, 2);
    encoder.addFieldTruncationRule("int3", INTEGER, 2);
    SortedMap<String, Object> immutableSortedMap =
        ImmutableSortedMap.of(
            "int1", (Object) 98,
            "int2", (Object) 12,
            "int3", (Object) 5);
    for (Entry<String, Object> entry : immutableSortedMap.entrySet()) {
      RuleEncoder rule = RuleEncoder.Builder.builder(INTEGER).width(2).build();
      when(paramEncoderObject.encode(entry, rule)).thenReturn(entry.getValue().toString());
    }

    String result = encoder.encode(immutableSortedMap);
    assertEquals("int1=98&int2=12&int3=5", result);
    verifyZeroInteractions(paramEncoderArray);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingParamEncoderObject() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("ParamEncoderObject is missing.");
    new ParamEncoderImpl(null, paramEncoderArray);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingParamEncoderArray() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("ParamEncoderArray is missing.");
    new ParamEncoderImpl(paramEncoderObject, null);
  }
}
