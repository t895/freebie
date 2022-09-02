package com.t895.freebie;

public interface UIManager
{
  // Collapse now playing panel and remove irrelevant information
  void songFinished();

  // Start now playing panel collapsed with new song information
  void setActiveSong();
}
