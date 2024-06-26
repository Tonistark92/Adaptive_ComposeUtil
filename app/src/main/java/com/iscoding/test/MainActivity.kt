package com.iscoding.test

import android.annotation.SuppressLint
import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.iscoding.test.ui.theme.TestTheme
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import java.util.Locale


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    //    private val localizationDelegate = LocalizationActivityDelegate(this)
//    val currentLanguage: Locale
//        get() = localizationDelegate.getLanguage(this)
//    override fun attachBaseContext(newBase: Context) {
//        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
//        val locale = "ar" // Change this to the language code you want to switch to
//        super.attachBaseContext(LocaleHelper.setLocale(newBase, "ar"))
//    }
//
//    override fun getApplicationContext(): Context {
//        return localizationDelegate.getApplicationContext(super.getApplicationContext())
//    }
//
//    override fun getResources(): Resources {
//        return localizationDelegate.getResources(super.getResources())
//    }
//    override fun onBeforeLocaleChanged() {}
//    override fun onAfterLocaleChanged() {}
//
//    fun setLanguage(language: String?) {
//        localizationDelegate.setLanguage(this, language!!)
//    }
    /////////////////
    ////// this is the final good method for all devices for localization
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.updateLocale(it) })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        localizationDelegate.addOnLocaleChangedListener(this)
//        localizationDelegate.onCreate()
        ///////////

        setContent {
            TestTheme {

                // for the configs
                val confg = Configuration()
                val c = LocalConfiguration.current
                /////////////
                val windowInfo = rememberWindowInfo()
                val items = listOf(
                    NavigationRailItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        hasNews = false,
                    ),
                    NavigationRailItem(
                        title = "Chat",
                        selectedIcon = Icons.Filled.Email,
                        unselectedIcon = Icons.Outlined.Email,
                        hasNews = false,
                        badgeCount = 45
                    ),
                    NavigationRailItem(
                        title = "Settings",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                        hasNews = true,
                    ),
                )
//            method #1

//            val sizeClass = calculateWindowSizeClass(activity = this)
//
//            val showOnePanel = sizeClass.widthSizeClass == WindowWidthSizeClass.Compact
//            when (sizeClass.widthSizeClass) {
//                WindowWidthSizeClass.Compact -> {
//                    BottomNavigation()
//                }
//
//                WindowWidthSizeClass.Medium -> {
//                    MyNavigationRail(
//                        items = items,
//                    )
//                }
//
//                WindowWidthSizeClass.Expanded -> {
//                    PermanentNavigationDrawer()
//
//                }
//
//                else -> {
//                    BottomNavigation()
//                }
//            }

//            method #2 makes sins (better XD)

                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
                    BottomNavigation()

                } else if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Medium) {
                    MyNavigationRail(
                        items = items,
                    )
                } else if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded) {
                    PermanentNavigationDrawer()

                } else {
                    BottomNavigation()

                }
            }
        }

    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun BottomNavigation(
    ) {
        val context = LocalContext.current
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        var locale by remember { mutableStateOf(Locale.getDefault()) }
        var layoutDirection by remember { mutableStateOf(LayoutDirection.Ltr) }
        var configuration = Configuration()
        configuration = resources.configuration.apply {
            setLocale(resources.configuration.locales[0])
        }
        CompositionLocalProvider(
            LocalAppLocale provides locale,
            LocalLayoutDirection provides layoutDirection,
//            LocalConfiguration provides configuration
        ) {


            // important
//            val lolLocal = ConfigurationCompat.getLocales(LocalConfiguration.current)[0]
//            ConfigurationCompat.setLocales()


            val context = LocalContext.current
            val configuration = LocalConfiguration.current
//          val configuration = context.resources.configuration
            // if in activity
//          val configuration = context.resources.configuration.config

            Scaffold(bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedItemIndex == 0,
                        onClick = { selectedItemIndex = 0 },
                        label = { Text("Home") },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") }
                    )
                    NavigationBarItem(
                        selected = selectedItemIndex == 1,
                        onClick = { selectedItemIndex = 1 },
                        label = { Text("Search") },
                        icon = { Icon(Icons.Filled.Search, contentDescription = "Search") }
                    )
                }
            }) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedItemIndex == 0) {
                        Text(
                            text = "HI To Compact Screen 1" + stringResource(id = R.string.hello),
                            fontSize = 18.ssp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Button(onClick = {
                            Log.d("ISLAM", "BUTTON CLICKED *********")
//                            locale = Locale("de")
//                            layoutDirection= LayoutDirection.Ltr
//                            val config = Configuration(context.resources.configuration)
//                            config.setLocale( Locale("de"))
//                            context.resources.updateConfiguration(config, context.resources.displayMetrics)
//                        setLanguage("ar")
                            locale = Locale("ar")

//                            val localeList = LocaleListCompat.create(Locale("ar"))
//                            ConfigurationCompat.setLocales(configuration, localeList)
//                            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
//                            context.createConfigurationContext(configuration)
//                            (context as? Activity)?.recreate()
//                            val wrappedContext = MyContextWrapper.wrap(context, "ar")
//                            LocaleHelper.setLocale(context, "ar")

                            // Restart the activity to apply the new locale
//                            if (context is Activity) {
//                                (context as Activity).apply {
//                                    finish()
//                                    startActivity(intent)
//                                    overridePendingTransition(0, 0)
//                                }
//                            }

                            /////////////////
                            ////// this is the final good method for all devices
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                changeLanguage("ar", this@MainActivity)

                            } else {
                                changeLanguage("ar", this@MainActivity)
                                (context as? Activity)?.recreate()
                            }
                        }, modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
                            Text(
                                text = stringResource(id = R.string.translate),
                                fontSize = 18.ssp,
                                color = MaterialTheme.colorScheme.tertiary
                            )

                        }
                        Button(onClick = {
                            Log.d("ISLAM", "BUTTON CLICKED *********")
//                            locale = Locale("de")
//                            layoutDirection= LayoutDirection.Ltr
//                            val config = Configuration(context.resources.configuration)
//                            config.setLocale( Locale("de"))
//                            context.resources.updateConfiguration(config, context.resources.displayMetrics)
//                        setLanguage("ar")
                            locale = Locale("ar")

//                            val localeList = LocaleListCompat.create(Locale("ar"))
//                            ConfigurationCompat.setLocales(configuration, localeList)
//                            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
//                            context.createConfigurationContext(configuration)
//                            (context as? Activity)?.recreate()
//                            val wrappedContext = MyContextWrapper.wrap(context, "ar")
//                            LocaleHelper.setLocale(context, "ar")

                            // Restart the activity to apply the new locale
//                            if (context is Activity) {
//                                (context as Activity).apply {
//                                    finish()
//                                    startActivity(intent)
//                                    overridePendingTransition(0, 0)
//                                }
//                            }

                            /////////////////
                            ////// this is the final good method for all devices
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                changeLanguage("en", this@MainActivity)

                            } else {
                                changeLanguage("en", this@MainActivity)
                                (context as? Activity)?.recreate()
                            }
                        }, modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
                            Text(
                                text = stringResource(id = R.string.translate),
                                fontSize = 18.ssp,
                                color = MaterialTheme.colorScheme.tertiary
                            )

                        }
                        Button(onClick = {
                            Log.d("ISLAM", "BUTTON CLICKED *********")
//                            locale = Locale("ar")
//                            layoutDirection= LayoutDirection.Ltr
//                            val config = Configuration(context.resources.configuration)
//                            config.setLocale( Locale("ar"))
//                            context.resources.updateConfiguration(config, context.resources.displayMetrics)
//                            setLanguage("de")
                            locale = Locale("de")
//                            val localeList = LocaleListCompat.create(Locale("de"))
//                            ConfigurationCompat.setLocales(configuration, localeList)
////                            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
//
//                            context.createConfigurationContext(configuration)
//                            (context as? Activity)?.recreate()
//                            val wrappedContext = MyContextWrapper.wrap(context, "de")
//                            LocaleHelper.setLocale(context, "de")
                            // Restart the activity to apply the new locale
//                            if (context is Activity) {
//                                (context as Activity).apply {
//                                    finish()
//                                    startActivity(intent)
////                                    overrideActivityTransition(0, 0, 0)
//                                }
//                            }
                            /////////////////
                            ////// this is the final good method for all devices
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                changeLanguage("de", this@MainActivity)

                            } else {
                                changeLanguage("de", this@MainActivity)
                                (context as? Activity)?.recreate()
                            }
                        }, modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
                            Text(
                                text = stringResource(id = R.string.translate),
                                fontSize = 18.ssp,
                                color = MaterialTheme.colorScheme.tertiary
                            )

                        }
                    } else {
                        Text(text = "HI To Compact Screen 2", fontSize = 18.ssp)

                    }

                }

            }
        }
    }


    fun changeLanguage(languageTag: String, context: Context) {
        val newLocale = Locale.forLanguageTag(languageTag)
        LocaleHelper.setLocale(context, newLocale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = android.os.LocaleList.forLanguageTags(languageTag)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
        }

    }

    data class NavigationRailItem(
        val title: String,
        val unselectedIcon: ImageVector,
        val selectedIcon: ImageVector,
        val hasNews: Boolean,
        val badgeCount: Int? = null
    )

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MyNavigationRail(
        items: List<NavigationRailItem>,
        // here if the roo nav is hosted u should add on navigate lambda
    ) {
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 40.dp)
        ) {

            NavigationRail(
                header = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .offset(x = (-1).dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.sdp, Alignment.Bottom)
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationRailItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                            },
                            icon = {
                                NavigationIcon(
                                    item = item,
                                    selected = selectedItemIndex == index
                                )
                            },
                            label = {
                                Text(text = item.title)
                            },
                        )
                    }
                }
            }
            // here u put the navhost and so on
            if (selectedItemIndex == 0) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "HI To Medium Screen for Rail 1", fontSize = 18.ssp)

                }
            } else if (selectedItemIndex == 1) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "HI To Medium Screen for Rail 2", fontSize = 18.ssp)


                }

            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "HI To Medium Screen for Rail 3", fontSize = 18.ssp)

                }

            }

        }


    }

    @Composable
    fun NavigationIcon(
        item: NavigationRailItem,
        selected: Boolean
    ) {
        BadgedBox(
            badge = {
                if (item.badgeCount != null) {
                    Badge {
                        Text(text = item.badgeCount.toString())
                    }
                } else if (item.hasNews) {
                    Badge()
                }
            }
        ) {
            Icon(
                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title
            )
        }
    }


    @Composable
    fun PermanentNavigationDrawer() {
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            PermanentDrawerSheet {
                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = selectedItemIndex == 0,
                    onClick = { selectedItemIndex = 0 }
                )
                NavigationDrawerItem(
                    label = { Text("Search") },
                    selected = selectedItemIndex == 1,
                    onClick = { selectedItemIndex = 1 }
                )
            }
            if (selectedItemIndex == 0) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HI To Expanded Screen for PermanentNavigationDrawer 1",
                        fontSize = 18.ssp
                    )

                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HI To Expanded Screen for PermanentNavigationDrawer 2",
                        fontSize = 18.ssp
                    )


                }

            }


        }

    }

    @Composable
    fun ProfileVerticalHorizantal(
        // the data
//        user: User
    ) {

        BoxWithConstraints(modifier = Modifier.padding(16.dp)) {
            when (this.maxWidth) {
                in (0.dp..400.dp) -> {
//                    VerticalProfile(user)
                }

                in (401.dp..900.dp) -> {
//                    HorizontalProfile(user)
                }
            }
        }
    }

    @Composable
    fun ProfileCompatExpanded(
//        user: User
    ) {
        BoxWithConstraints(modifier = Modifier.padding(16.dp)) {
            when (this.maxWidth) {
                in (0.dp..600.dp) -> {
//                    CompactProfile(user)
                }

                in (601.dp..900.dp) -> {
//                    ExpandedProfile(user)
                }
            }
        }
    }

}




