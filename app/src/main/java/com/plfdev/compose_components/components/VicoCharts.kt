package com.plfdev.compose_components.components

import android.graphics.Typeface
import android.text.Layout
import android.text.TextUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.Position
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.DashedShape
import com.patrykandpatrick.vico.core.common.shape.Shape
import java.text.DecimalFormat

private val yFormat = DecimalFormat("#.##")

val xLabels = listOf(
    "01\nJan\n2023", "02\nFev\n2023", "03\nMai\n2023", "04\nJan\n2023",
    "05\nJan\n2023", "06\nJan\n2023", "07\nDec\n2024", "08\nJan\n2025",
    "12\nJan\n2025",
    "14\nFev\n2025",
)

private val y =
    listOf(
        0.40,
        0.81,
        0.53,
        0.77,
        0.98,
        0.99,
        1.22
    )

@Composable
fun VicoCharts(
    modifier: Modifier = Modifier,
    minRef: Double = 0.52,
    maxRef: Double = 0.87,
    paddingVertical: Double = 0.1,
) {
    val scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.Start)
    val modelProducer = remember { CartesianChartModelProducer() }


    //region Text Bottom Label X Axis

    val bottomFormatter = remember(xLabels) {
        CartesianValueFormatter { _, value, _ ->
            xLabels.getOrNull(value.toInt()) ?: ""
        }
    }

    val bottomLabelComponent = rememberAxisLabelComponent(
        lineCount = 3,
        padding = Insets(
            horizontalDp = 0.dp.value,
            verticalDp = 2.dp.value
        ), // <- remove padding que pode causar truncamento :contentReference[oaicite:1]{index=1}
        margins = Insets(horizontalDp = 0.dp.value, verticalDp = 0.dp.value),
        textAlignment = Layout.Alignment.ALIGN_CENTER
    ) // assinatura tem lineCount/overflow/padding/margins :contentReference[oaicite:2]{index=2}

    //endregion

    //region Data Label

    val dataLabelComponent = rememberTextComponent(
        color = Color(0xFF000000),
        typeface = Typeface.DEFAULT_BOLD, // ✅ bold :contentReference[oaicite:2]{index=2}
        textSize = 12.sp,
        textAlignment = Layout.Alignment.ALIGN_CENTER,
        lineCount = 1,
        truncateAt = TextUtils.TruncateAt.END,
    )

    val yValueFormatter = remember {
        CartesianValueFormatter.decimal(yFormat)
    }

    //endregion

    //region Columns

    val columnShape = CorneredShape.rounded(
        topLeftPercent = 40,
        topRightPercent = 40,
        bottomLeftPercent = 40,
        bottomRightPercent = 40,
    )

    val greenColumn = rememberLineComponent(
        fill = fill(Color(0xff0ac285)),
        thickness = 8.dp,
        shape = columnShape
    )

    val yellowColumn = rememberLineComponent(
        fill = fill(Color(0xffffc107)), // amarelo
        thickness = 8.dp,
        shape = columnShape,
    )

    val conditionalProvider = remember(greenColumn, yellowColumn) {
        object : ColumnCartesianLayer.ColumnProvider {
            override fun getColumn(
                entry: ColumnCartesianLayerModel.Entry,
                seriesIndex: Int,
                extraStore: ExtraStore
            ): LineComponent = if (entry.y > maxRef) yellowColumn else greenColumn

            override fun getWidestSeriesColumn(
                seriesIndex: Int,
                extraStore: ExtraStore
            ): LineComponent = greenColumn
        }
    }

    //endregion

    //region Lines

    val dashedRefLine = rememberLineComponent(
        fill = fill(Color(0xFFBDBDBD)),
        thickness = 1.dp,
        shape = DashedShape(
            shape = Shape.Rectangle,
            dashLengthDp = 6f, // tamanho do traço
            gapLengthDp = 4f,  // tamanho do espaço
        )
    )

    val refLabel = rememberTextComponent(
        color = Color(0xFFBDBDBD),
        textAlignment = Layout.Alignment.ALIGN_NORMAL, // label alinhado à esquerda
    )

    val decorations = remember(minRef, maxRef, dashedRefLine, refLabel) {
        listOf(
            HorizontalLine(
                y = { maxRef },
                line = dashedRefLine,
                labelComponent = refLabel,
                label = { maxRef.toString() }, // "190"
                horizontalLabelPosition = Position.Horizontal.Start,
                verticalLabelPosition = Position.Vertical.Top, // label em cima da linha
                verticalAxisPosition = Axis.Position.Vertical.Start,
            ),
            HorizontalLine(
                y = { minRef },
                line = dashedRefLine,
                labelComponent = refLabel,
                label = { minRef.toString() }, // "150"
                horizontalLabelPosition = Position.Horizontal.Start,
                verticalLabelPosition = Position.Vertical.Top,
                verticalAxisPosition = Axis.Position.Vertical.Start,
            ),
        )
    }

    //endregion

    val rangeProvider = remember {
        val auto = CartesianLayerRangeProvider.auto()
        object : CartesianLayerRangeProvider {
            override fun getMinX(minX: Double, maxX: Double, extraStore: ExtraStore) =
                auto.getMinX(minX, maxX, extraStore)

            override fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore) =
                auto.getMaxX(minX, maxX, extraStore)

            override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
                val padBottom = (maxY - minY) * paddingVertical   // 20% de “respiro” embaixo
                return minY - padBottom
            }

            override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
                val headroom = (maxY - minY) * paddingVertical // 20% de folga no topo
                return maxY + headroom
            }
        }
    }

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(y) // x = 0..7 automaticamente :contentReference[oaicite:3]{index=3}
            }
        }
    }

    Box {
        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = conditionalProvider,
                    columnCollectionSpacing = 64.dp,
                    rangeProvider = rangeProvider,
                    dataLabel = dataLabelComponent,                 // ✅ mostra acima das barras
                    dataLabelPosition = Position.Vertical.Top,      // (default já é Top)
                    dataLabelValueFormatter = yValueFormatter,      // ✅ valor do Y
                    dataLabelRotationDegrees = 0f,
                ),

                // REMOVE eixo Y (labels) + REMOVE grade horizontal
                startAxis = VerticalAxis.rememberStart(
                    line = null,
                    label = null,      // remove os valores do Y
                    tick = null,
                    guideline = null,  // remove grade horizontal
                ),

                // MANTÉM só eixo X (1940..1947) + REMOVE grade vertical
                bottomAxis = HorizontalAxis.rememberBottom(
                    label = bottomLabelComponent,
                    valueFormatter = bottomFormatter,
                    labelRotationDegrees = 0f,
                    line = null, tick = null, guideline = null, // remove TODAS as linhas do bottom
                ),
                layerPadding = {
                    CartesianLayerPadding(
                        unscalableStartDp = 24.dp.value,
                        unscalableEndDp = 24.dp.value
                    )
                },
                decorations = decorations
            ),
            modelProducer = modelProducer,
            modifier = modifier.height(280.dp),
            scrollState = scrollState,
        )

        ChartScrollbar(
            scrollState = scrollState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 34.dp)
        )
    }
}