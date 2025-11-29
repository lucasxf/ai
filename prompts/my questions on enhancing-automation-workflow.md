# enhancing-automation-workflow review

My considerations on the approaches

1. Stateful Delta Tracking
   1. why can't it also leverage git?
   2. why didn't you recommend it to be stored in `.claude/` (currently git tracked) instead of `/prompts`?
   3. would be benefit from using Toon format instead of JSON in this session (or in any other sessions for that matter), as well as in the proposed metric file schema?
   4. I like option B. But in that case, should I pick `--mode=delta`, will the resulting metrics file have a consolidated view off ALL the metrics usage (like counting and summing the outputs of the previous run to the current one, so that I can see the bigger picture? (without running full analysis, obviously))
   5. This is my preferred approach. Answer the questions above and go ahead (event-sourcing was indeed overkill, and I don't want polluted commits)
2. I like the `metrics-collector` approach. But I don't like its name. Too boring, too on the nose.
3. You can skip consolidating the tests commands for now
4. I don't deploy that often. We can skip `deployment-manager` for now
5. We don't need to split up `tech-writer` yet
6. stay on the same branch then
7. go ahead with the changes and commit them