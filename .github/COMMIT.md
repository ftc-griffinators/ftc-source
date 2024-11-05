# Commit Message Convention

## Format

```
<type>(<scope>): <subject>

[optional body]

[optional footer(s)]
```

## Types

- `feat`: New feature for the robot/code
- `fix`: Bug fix
- `chore`: Changes to build process, project setup, or other maintenance
- `docs`: Documentation only changes
- `style`: Code style/formatting changes
- `refactor`: Code changes that neither fix bugs nor add features
- `perf`: Performance improvements
- `test`: Adding or modifying tests
- `auto`: Changes to autonomous code
- `teleop`: Changes to teleop code
- `vision`: Changes to vision/camera code
- `tuning`: Robot tuning and calibration changes

## Scopes (optional)

- `drive` - Drivetrain related
- `arm` - Arm mechanism
- `intake` - Intake system
- `outtake` - Outtake system
- `vision` - Vision/camera systems
- `auto` - Autonomous routines
- `pid` - PID tuning
- `ci` - CI/CD changes
- `deps` - Dependency updates

## Examples

```
feat(arm): add new arm position presets
fix(drive): correct strafe drift compensation
chore: init project
docs(auto): update autonomous routine documentation
style: format according to team style guide
test(vision): add tests for april tag detection
auto(red): implement red alliance autonomous
tuning(pid): adjust arm PID coefficients
```

## Special Cases

- Breaking changes: Add `BREAKING CHANGE:` in footer
- Multiple scopes: Use comma separated list `feat(drive,auto): ...`
- References: Add issue/PR references in footer `Refs: #123`

## References

- [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)