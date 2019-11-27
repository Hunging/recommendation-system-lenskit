package org.lenskit.mooc.cbf;

import java.util.ArrayList;
import org.lenskit.data.ratings.Rating;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Build a user profile from all positive ratings.
 */
public class WeightedUserProfileBuilder implements UserProfileBuilder {

  /**
   * The tag model, to get item tag vectors.
   */
  private final TFIDFModel model;

  @Inject
  public WeightedUserProfileBuilder(TFIDFModel m) {
    model = m;
  }

  @Override
  public Map<String, Double> makeUserProfile(@Nonnull List<Rating> ratings) {
    // Create a new vector over tags to accumulate the user profile
    Map<String, Double> profile = new HashMap<>();

    // TODO Normalize the user's ratings
    Double ratingavg = 0.0;
    for (Rating rating : ratings) {
      ratingavg += rating.getValue();
    }
    ratingavg /= ratings.size();
    for (Rating r : ratings) {
      Map<String, Double> itemVector = model.getItemVector(r.getItemId());
      double ratingValue = r.getValue() - ratingavg;
      for (Map.Entry<String, Double> e : itemVector.entrySet()) {
        String key = e.getKey();
        Double value = ratingValue * e.getValue();
        if (profile.containsKey(key)) {
          value += profile.get(key);
        }
        profile.put(key, value);
      }
    }
    // TODO Build the user's weighted profile
    // The profile is accumulated, return it.
    return profile;
  }
}
