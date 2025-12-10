## v???(dev)

## v0.8.2
+ Fixed a bug with the mod in SMP where player attributes for movement speed and breaking speed were not getting updated properly, leaving the player in a slowed state

## v0.8.1
+ Changed all configuration options in the mod to use the custom config library added by BTWR: Shared Library. This fixes the bug from the last version that crashed the game without any warnings of the missing library that created the configurations
+ Removed Supermartijn642's config lib as the one creating configuration setting as it requires itself as a dependency to work properly
+ Updated the mod to BTWR: Shared Library 0.6.5

## v0.8(or 1.8)
+ Added new datagen classes internally for making json files for new sound events by mods.
+ Changed versioning scheme from "1.x" to "0.x" for the beta releases. The mod is pretty much feature complete, but there might be some additions until I decide to make it official "release" versioning
+ Changed the "ModPenalties" code internally and made added a new API class "PenaltyTextHelper". It contains helper methods for developers to create penalty texts and also other helper methods for the default hunger, health & fat calculations
+ Changed all configuration options setting to be handled with Supermartijn642's Config Lib internally instead of Cloth Config API, which is used only for client side config options. Cloth Config is still used for creating all screens for access through Mod Menu
+ Updated the mod to BTWR: Shared Library 0.6.4

## v1.7
+ Added check and a configuration option for the player making ambient hurt noises while sneaking. This is probably useful for SMP and especially PvP situations where hurt players wouldn't want to be heard by other players trying to kill them
+ Added a configuration option to set the hunger level at which sprinting should be disabled. By default, it's 8.0 (Four hunger shanks (Peckish))
+ Fixed the player making ambient hurt noises while flying with an elytra
+ Split client side logic into its own package
+ Updated the mod to Fabric API 0.116.7, & Fabric Loader 0.17.3 & BTWR: Shared Library 0.62

## v1.6
This update brings compatibility with the Granular Hunger mod by Tetro48 and a few more bug fixes/features for the mod from him as well. Thanks for those :)

#### Features related to Granular Hunger: 
- Configuration option to toggle if fat penalties should apply overall.
- Added display texts for Fat penalties from the Granular Hunger mod to display when hunger gets lower.
- Code changes to account if the mod is installed and get the proper hunger value in that case for displaying hunger penalties
- Fixed a bug with penalties like crippled or famished stacking their effects in a wrong way causing different values than expected

#### Other changes:
+ Added functionality where the player's attack damage and block breaking speed will get affected when at low health/hunger as well
+ Fixed (added) missing hunger level display state ("Emaciated") to display when the player is 1 shank or lower (after "Famished") 
+ Updated the mod to Fabric API 0.115.6 + & Fabric Loader 0.16.13 (ivangeevo)

## v1.5

#### Another set of changes by jeffyjamzhd
+ Integrate BTWRSL as a new dependency, pulling from the GitHub maven
+ Update translation keys to all use the same namespace im_movens
+ Translation keys have been added for all penalties and the ambient pain noises
+ Rendering for health and hunger statuses have been altered to better match BTW
+ Thresholds for certain penalties have been updated to reflect the above change
+ Added a penalty for the dev environment that shows raw health and hunger values only when F3 is toggled


## v1.4
+ Fixed mod icon to display properly in the mod lists.
+ Updated mod description to display properly in the mod lists & updated to correct license (CC-BY-4.0)
+ Updated the mod to Fabric Loader 0.16.10 & Fabric API 0.115.3

#### Big thanks to jeffyjamzhd for providing some big quality of life changes.

#### They are as follows:

+ Fix issues that are present with the existing renderer
+ Have closer parity with Better Than Wolves
+ Have implicit compatibility with mods that alter how the hotbar is rendered, and those that rearrange and remove status bars

#### New functionality has also been added:

+ Penalties no longer affect the player in creative, spectator, or on death
+ Disable FOV scaling when penalties are in effect, which is compatible with mods with a similar option (e.g. Blockrunner)
+ Ambient pain sounds when health penalties are in effect, emulating the system seen in older versions of Better Than Wolves

## v1.3
+ Added conditions to account for the air bar displaying and move the status text appropriately
+ Updated the mod to Fabric API 0.110.0

## v1.2
+ Changed mod id from "im-movens" to "im_movens"
+ Improved code for better compatibility with other mods.
+ Updated the mod to Fabric API 0.108.0 & Fabric Loader 0.16.9

## v1.1
+ Added increased exhaustion from jumping & even more from sprinting/sprint jumping

## v1.0
+ Initial Release