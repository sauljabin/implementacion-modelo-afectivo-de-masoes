satisfactionByAction(AGENT, greeting, positive_high) :- other(AGENT).
satisfactionByAction(AGENT, smile, positive_low) :- other(AGENT).
satisfactionByAction(AGENT, run, negative_low) :- other(AGENT).
satisfactionByAction(AGENT, bye, negative_high) :- other(AGENT).
satisfactionByAction(AGENT, eat, positive_high) :- self(AGENT).
satisfactionByAction(AGENT, sleep, positive_low) :- self(AGENT).
satisfactionByAction(AGENT, wake, negative_low) :- self(AGENT).
satisfactionByAction(AGENT, pay, negative_high) :- self(AGENT).
satisfactionByObject(AGENT, PROPERTIES, positive_high) :- member(color=blue, PROPERTIES), anyone(AGENT).
satisfactionByObject(AGENT, PROPERTIES, positive_low) :- member(color=red, PROPERTIES), anyone(AGENT).
satisfactionByObject(AGENT, PROPERTIES, negative_low) :- member(color=white, PROPERTIES), anyone(AGENT).
satisfactionByObject(AGENT, PROPERTIES, negative_high) :- member(color=black, PROPERTIES), anyone(AGENT).