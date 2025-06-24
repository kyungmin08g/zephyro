package com.github.kyungmin08g.zephyro.core.logger.event;

import com.github.kyungmin08g.zephyro.core.utils.enums.Level;
import com.github.kyungmin08g.zephyro.core.utils.enums.LevelColor;
import lombok.*;

@Setter
@NoArgsConstructor
public class ZephyroLogEvent {
  private String message;
  private Class<?> clazz;
  private Level level;
  private LevelColor levelColor;

  public String getMessage() {
    return getMessageFormat(message, clazz, levelColor, level);
  }

  private String getMessageFormat(String message, Class<?> clazz, LevelColor color, Level level) {
    return String.format(
      "%s[%s]%s %s%s%s : %s",
      color.getColor(), level,
      LevelColor.RESET.getColor(), LevelColor.WHITE.getColor(),
      String.valueOf(clazz).split(" ")[1],
      LevelColor.RESET.getColor(),
      message
    );
  }
}
